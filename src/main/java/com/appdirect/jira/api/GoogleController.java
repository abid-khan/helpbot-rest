package com.appdirect.jira.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.helper.google.OAuthHelper;
import com.appdirect.jira.props.OAuth;
import com.appdirect.jira.service.GoogleCalendarService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

/**
 * Created by abidkhan on 29/04/17.
 */
@Component
@Path("/google/calendar")
@Slf4j
@EnableConfigurationProperties(OAuth.class)
public class GoogleController {
    @Autowired
    private GoogleAuthorizationCodeFlow flow;
    @Autowired
    private OAuth oAuth;
    @Autowired
    private OAuthHelper OAuthHelper;
    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GET
    @Path("/meetings")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response meetings(@QueryParam("userId") String userId) {
        try {
            log.info("Request received to get meetings for userId {}", userId);
            Credential credential = OAuthHelper.loadCredential(userId);
            if (null == credential) {
                log.info("No credential present for userId {}.Generating oauth URL", userId);
                //Credential not present;ask user to authorize calendar access
                return Response.serverError().entity(OAuthHelper.getOAuthUrl(userId)).type(MediaType.APPLICATION_JSON).build();
            }
            log.info("Found saved credential for userId {}. Fetching meeting details", userId);
            return Response.status(200).entity(googleCalendarService.findMeetings(credential)).type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(null).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
