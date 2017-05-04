package com.appdirect.jira.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.appdirect.jira.api.GoogleController;

/**
 * Created by abidkhan on 21/04/17.
 */
@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Exception Mapper
        packages("com.appdirect.jira");
        register(GoogleController.class);
    }
}
