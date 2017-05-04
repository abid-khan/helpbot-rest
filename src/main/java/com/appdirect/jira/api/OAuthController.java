package com.appdirect.jira.api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.appdirect.jira.helper.google.OAuthHelper;

/**
 * Created by abidkhan on 28/04/17.
 */
@Controller
@RequestMapping("/calendar")
@Slf4j
public class OAuthController {
    @Autowired
    private OAuthHelper OAuthHelper;

    @RequestMapping(method = RequestMethod.GET,path = "/google",produces ={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String calendar(Model model, @RequestParam("userId") String userId) {
        String url = OAuthHelper.getOAuthUrl(userId);
        log.info("Generated oauth URL for userId {} as {}", userId, url);
        model.addAttribute("calendarLink", url);
        return "google";
    }
}
