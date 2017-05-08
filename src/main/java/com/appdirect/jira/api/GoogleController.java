package com.appdirect.jira.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

import com.appdirect.jira.helper.google.GoogleOAuthHelper;
import com.appdirect.jira.props.OAuth;
import com.appdirect.jira.service.GoogleCalendarService;
import com.appdirect.jira.vo.Meetings;
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
    private GoogleOAuthHelper GoogleOAuthHelper;
    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GET
    @Path("/meetings")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response meetings(@QueryParam("userId") String userId, @DefaultValue("10") @QueryParam("maxCount") int maxCount) {
        Meetings meetings;
        try {
            log.info("Request received to get meetings for userId {}", userId);
            Credential credential = GoogleOAuthHelper.loadCredential(userId);
            if (null == credential) {
                log.info("No credential present for userId {}.Generating oauth URL", userId);
                meetings = Meetings.builder().oAuthUrl(GoogleOAuthHelper.getOAuthUrl(userId)).build();
            } else {
                log.info("Found saved credential for userId {}. Fetching meeting details", userId);
                meetings = googleCalendarService.findMeetings(credential, maxCount);
            }
            return Response.status(200).entity(meetings).type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(null).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
