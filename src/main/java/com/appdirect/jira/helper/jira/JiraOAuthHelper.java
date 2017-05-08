package com.appdirect.jira.helper.jira;

import static net.oauth.OAuth.OAUTH_VERIFIER;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.props.Authentication;
import com.google.common.collect.ImmutableList;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

/**
 * Created by abidkhan on 07/05/17.
 */
@Component
@EnableConfigurationProperties(Authentication.class)
@Slf4j
public class JiraOAuthHelper {
    @Autowired
    private OAuthClient oAuthClient;
    @Autowired
    private OAuthAccessorHelper oAuthAccessorHelper;
    @Autowired
    private Authentication authentication;

    /**
     * Generate request token for user
     *
     * @param userId
     * @return OAuthHolder
     */
    public OAuthHolder getRequestToken(String userId) {
        OAuthAccessor oAuthAccessor = null;
        try {
            oAuthAccessor = oAuthAccessorHelper.oAuthAccessor();
            List<OAuth.Parameter> callBack;
            if (authentication.getCallbackUrl() == null || "".equals(authentication.getCallbackUrl())) {
                callBack = Collections.<OAuth.Parameter>emptyList();
            } else {
                callBack = ImmutableList.of(new OAuth.Parameter(OAuth.OAUTH_CALLBACK, authentication.getCallbackUrl()));
            }

            OAuthMessage message = oAuthClient.getRequestTokenResponse(oAuthAccessor, OAuthMessage.POST, callBack);
            OAuthHolder tokenSecretVerifier = OAuthHolder.builder().token(oAuthAccessor.requestToken).secret(oAuthAccessor.tokenSecret).verifier(message.getParameter(OAUTH_VERIFIER)).build();
            log.info("Received OAuthHolder {}", tokenSecretVerifier.toString());
            return tokenSecretVerifier;
        } catch (Exception ex) {
            log.error("Failed due to {}");
        }

        return null;
    }

    /**
     * Generate accesstoken for user
     *
     * @param requestToken
     * @param tokenSecret
     * @param oauthVerifier
     * @return accesstoken
     */
    public String getAccessToken(String requestToken, String tokenSecret, String oauthVerifier) {
        String accessToken = null;
        OAuthAccessor oAuthAccessor = null;
        try {
            oAuthAccessor = oAuthAccessorHelper.oAuthAccessor();
            OAuthClient client = new OAuthClient(new HttpClient4());
            oAuthAccessor.requestToken = requestToken;
            oAuthAccessor.tokenSecret = tokenSecret;
            OAuthMessage message = client.getAccessToken(oAuthAccessor, "POST",
                    ImmutableList.of(new OAuth.Parameter(OAuth.OAUTH_VERIFIER, oauthVerifier)));
            accessToken = message.getToken();
        } catch (Exception ex) {
            log.error("Failed to get accessToken  due to {}", ex);
        }

        log.info("Got accessTone {}", accessToken);
        return accessToken;
    }

    /**
     * @param token
     * @return oauth URL
     */
    public String getAuthorizeUrlForToken(String token) {
        String url = authentication.getBaseUrl() + authentication.getAuthorizationUrl() + "?oauth_token=" + token;
        log.info("Jira OAuth url {}", url);
        return url;
    }


}
