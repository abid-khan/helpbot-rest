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
    private String userName;
    private String password;
    private String url;
}
