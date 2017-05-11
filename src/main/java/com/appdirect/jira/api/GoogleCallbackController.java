package com.appdirect.jira.api;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.appdirect.jira.entity.JiraUser;
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
    public ModelAndView success(@RequestParam("code") String code, @RequestParam("state") String state) throws IOException {
        log.info("code is {} and state is {}", code, state);
        final GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(oAuth.getGoogleCallback()).execute();
        final Credential credential = flow.createAndStoreCredential(response, state);
        ModelAndView mav = new ModelAndView("success");
        String[] tokens= state.split("-");
        JiraUser jiraUser = JiraUser.builder().userId(tokens[0]).channelId(tokens[1]).teamId(tokens[2]).build();
        mav.addObject("jiraUser", jiraUser);
        return mav;
    }



}
