package com.appdirect.jira.helper.google;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.props.OAuth;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;

/**
 * Created by abidkhan on 28/04/17.
 */
@Component
@Slf4j
@EnableConfigurationProperties(OAuth.class)
public class GoogleOAuthHelper {
    @Autowired
    private OAuth oAuth;

    @Autowired
    private GoogleAuthorizationCodeFlow flow;

    public String getOAuthUrl(String userId,String channelId, String teamId) {
        String loginUrl = null;
        try {
            final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
            return url.setRedirectUri(oAuth.getGoogleCallback()).setState(userId).build();
        } catch (Exception ex) {
            log.error("Failed to generate login url due to  {}", ex);
        }
        return null;
    }

    public String buildState(String userId,String channelId, String teamId){
        return  userId.concat("-").concat(channelId).concat("-").concat(teamId);
    }
    /**
     * @param userId
     * @return
     * @throws IOException
     */
    public Credential loadCredential(String userId) throws IOException {
        log.info("Loading saved credential for userId {}", userId);
        return flow.loadCredential(userId);
    }
}
