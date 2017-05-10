package com.appdirect.jira.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appdirect.jira.props.OAuth;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

/**
 * Created by abidkhan on 28/04/17.
 */
@Configuration
@EnableConfigurationProperties(OAuth.class)
public class GoogleConfig {

    @Autowired
    private OAuth oAuth;
    private final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    @Bean("googleNetHttpTransport")
    public HttpTransport googleNetHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean("fileDataStoreFactory")
    public FileDataStoreFactory fileDataStoreFactory() throws IOException {
        return new FileDataStoreFactory(new java.io.File(oAuth.getDirectory()));
    }

    @Bean("jsonFactory")
    public JsonFactory jsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow(@Qualifier("googleNetHttpTransport") HttpTransport httpTransport, @Qualifier("fileDataStoreFactory") FileDataStoreFactory fileDataStoreFactory, @Qualifier("jsonFactory") JsonFactory jsonFactory) throws IOException, GeneralSecurityException {
        InputStream in = GoogleConfig.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, SCOPES)
                .setDataStoreFactory(fileDataStoreFactory)
                .setAccessType("offline")
                .build();
    }
}
