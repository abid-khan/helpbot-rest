package com.appdirect.jira.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appdirect.jira.props.Authentication;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

/**
 * Created by abidkhan on 20/04/17.
 */
@Configuration
@EnableConfigurationProperties(Authentication.class)
public class JiraConfig {

    @Autowired
    private Authentication authentication;

    @Bean
    public AsynchronousJiraRestClientFactory asynchronousJiraRestClientFactory() {
        return new AsynchronousJiraRestClientFactory();
    }

    @Bean("oAuthClient")
    public OAuthClient oAuthClient() {
        return new OAuthClient(new HttpClient4());
    }
}
