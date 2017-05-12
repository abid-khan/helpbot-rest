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
import com.appdirect.jira.helper.jira.JiraOAuthHelper;
import com.appdirect.jira.repository.JiraUserRepository;

/**
 * Created by abidkhan on 07/05/17.
 */
@Controller
@RequestMapping("/jira")
@Slf4j
public class JiraCallbackController {
    @Autowired
    private JiraOAuthHelper jiraOAuthHelper;

    @Autowired
    private JiraUserRepository jiraUserRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/callback", produces = {org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView success(@RequestParam("oauth_token") String oAuthToken, @RequestParam("oauth_verifier") String oAuthVerifier) throws IOException {
        log.info("oAuthToken is {} and oAuthVerifier is {}", oAuthToken, oAuthVerifier);
        JiraUser jiraUser = jiraUserRepository.findByRequestToken(oAuthToken);
        String accessToken =  jiraOAuthHelper.getAccessToken(oAuthToken,jiraUser.getSecret(),oAuthVerifier);
        log.info("AccessToken {}",accessToken);
        jiraUser.setAccessToken(accessToken);
        jiraUser.setVerifirer(oAuthVerifier);
        jiraUserRepository.save(jiraUser);
        ModelAndView mav = new ModelAndView("jiraSuccess");
        mav.addObject("jiraUser", jiraUser);
        mav.addObject("slackLink", buildSlackLink(jiraUser.getTeamId(),jiraUser.getChannelId()));
        return  mav;
    }

    private String buildSlackLink(String teamId,String userId){
        return "slack://user?team="+teamId + "&id="+userId;
    }
}
