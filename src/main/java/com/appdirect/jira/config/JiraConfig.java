package com.appdirect.jira.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appdirect.jira.props.Authentication;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

/**
 * Created by abidkhan on 20/04/17.
 */
@Configuration
@EnableConfigurationProperties(Authentication.class)
public class JiraConfig {

    @Autowired
    private Authentication authentication;

    @Bean
    public AsynchronousJiraRestClientFactory asynchronousJiraRestClientFactory(){
        return  new AsynchronousJiraRestClientFactory();
    }

    @Bean
    public JiraRestClient jiraRestClient(@Qualifier("asynchronousJiraRestClientFactory") AsynchronousJiraRestClientFactory jiraRestClientFactory){
        URI endURI =  URI.create(authentication.getUrl());
       //return jiraRestClientFactory.create(endURI,new AnonymousAuthenticationHandler());
        return jiraRestClientFactory.createWithBasicHttpAuthentication(endURI, authentication.getUserName(),authentication.getPassword());
    }
}
