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
import org.springframework.stereotype.Component;

import com.appdirect.jira.entity.JiraUser;
import com.appdirect.jira.helper.google.GoogleOAuthHelper;
import com.appdirect.jira.helper.jira.JiraOAuthHelper;
import com.appdirect.jira.helper.jira.OAuthHolder;
import com.appdirect.jira.repository.JiraUserRepository;
import com.appdirect.jira.vo.JiraOauth;
import com.google.api.client.auth.oauth2.Credential;

/**
 * Created by abidkhan on 07/05/17.
 */
@Component
@Path("/oauth")
@Slf4j
public class OAuthController {
    @Autowired
    private JiraOAuthHelper jiraOAuthHelper;
    @Autowired
    private JiraUserRepository jiraUserRepository;

    @Autowired
    private GoogleOAuthHelper googleOAuthHelper;

    @GET
    @Path("/jira")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jira(@QueryParam("userId") String userId,@QueryParam("channelId") String channelId,@QueryParam("teamId") String teamId) {
        JiraOauth jiraOauth = null;
        JiraUser jiraUser = null;
        String url = null;
        try {
            jiraUser = jiraUserRepository.findByUserId(userId);

            if (null == jiraUser || jiraUser.getAccessToken() == null) {
                OAuthHolder OAuthHolder = jiraOAuthHelper.getRequestToken(userId);
                jiraUser = JiraUser.builder().userId(userId).requestToken(OAuthHolder.getToken()).secret(OAuthHolder.getSecret()).channelId(channelId).teamId(teamId).build();
                jiraUserRepository.save(jiraUser);
                url = jiraOAuthHelper.getAuthorizeUrlForToken(jiraUser.getRequestToken());
            }
            return Response.status(200).entity(JiraOauth.builder().url(url).build()).type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(null).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("/google")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response google(@QueryParam("userId") String userId,@QueryParam("channelId") String channelId,@QueryParam("teamId") String teamId) {
        JiraOauth jiraOauth = null;
        JiraUser jiraUser = null;
        String url = null;
        try {
            Credential credential = googleOAuthHelper.loadCredential(userId);
            if (null == credential) {
                log.info("No credential present for userId {}.Generating oauth URL", userId);
                url= googleOAuthHelper.getOAuthUrl(userId,channelId,teamId);
            }

            return Response.status(200).entity(JiraOauth.builder().url(url).build()).type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(null).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
