package com.appdirect.jira.api;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.appdirect.jira.props.OAuth;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

/**
 * Created by abidkhan on 29/04/17.
 */
@Controller
@RequestMapping("/google")
@Slf4j
public class GoogleCallbackController {
    @Autowired
    private GoogleAuthorizationCodeFlow flow;
    @Autowired
    private OAuth oAuth;


    @RequestMapping(method = RequestMethod.GET, path = "/callback", produces = {org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String success(@RequestParam("code") String code, @RequestParam("state") String userId) throws IOException {
        log.info("code is {} and userId is {}", code, userId);
        final GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(oAuth.getGoogleCallback()).execute();
        final Credential credential = flow.createAndStoreCredential(response, userId);
        return "success";
    }



}
