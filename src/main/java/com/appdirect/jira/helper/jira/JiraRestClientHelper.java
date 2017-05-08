package com.appdirect.jira.helper.jira;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.props.Authentication;
import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.ParameterStyle;
import net.oauth.client.OAuthClient;
import net.oauth.http.HttpMessage;

/**
 * Created by abidkhan on 08/05/17.
 */
@Component
@EnableConfigurationProperties(Authentication.class)
public class JiraRestClientHelper {
    @Autowired
    private Authentication authentication;

    @Autowired
    private OAuthAccessorHelper oAuthAccessorHelper;

    @Autowired
    @Qualifier("asynchronousJiraRestClientFactory")
    AsynchronousJiraRestClientFactory jiraRestClientFactory;

    /**
     * Create JiraRestClient
     * @param accessToken
     * @param secret
     * @return <code>JiraRestClient</code>
     */
    public JiraRestClient jiraRestClient(String accessToken, String secret) {
        URI endURI = URI.create(authentication.getUrl());
        return jiraRestClientFactory.create(endURI, new AuthenticationHandler() {
            @Override
            public void configure(Request request) {
                try {
                    OAuthAccessor oAuthAccessor = oAuthAccessorHelper.oAuthAccessor();
                    oAuthAccessor.accessToken=accessToken;
                    oAuthAccessor.tokenSecret=secret;
                    OAuthMessage request2 = oAuthAccessor.newRequestMessage(null, request.getUri().toString(), Collections.<Map.Entry<?, ?>>emptySet(), request.getEntityStream());
                    Object accepted = oAuthAccessor.consumer.getProperty(OAuthConsumer.ACCEPT_ENCODING);
                    if (accepted != null) {
                        request2.getHeaders().add(new OAuth.Parameter(HttpMessage.ACCEPT_ENCODING, accepted.toString()));
                    }
                    Object ps = oAuthAccessor.consumer.getProperty(OAuthClient.PARAMETER_STYLE);
                    ParameterStyle style = (ps == null) ? ParameterStyle.BODY
                            : Enum.valueOf(ParameterStyle.class, ps.toString());
                    HttpMessage httpRequest = HttpMessage.newRequest(request2, style);
                    for ( Map.Entry<String, String> ap : httpRequest.headers)
                        request.setHeader(ap.getKey(), ap.getValue());
                    request.setUri( httpRequest.url.toURI() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
