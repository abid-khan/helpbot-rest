package com.appdirect.jira.props;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by abidkhan on 20/04/17.
 */
@ConfigurationProperties(prefix = "jira")
@Getter
@Setter
public class Authentication {
    private String url;
    private String baseUrl;
    private String requestTokenUrl;
    private String authorizationUrl;
    private String accessTokenUrl;
    private String oauthSignType;
    private String consumerKey;
    private String consumerSecret;
    private String callbackUrl;
    private String privateKey;
}
