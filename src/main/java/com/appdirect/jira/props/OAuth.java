package com.appdirect.jira.props;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by abidkhan on 28/04/17.
 */
@ConfigurationProperties(prefix = "oauth")
@Getter
@Setter
public class OAuth {
    private String directory;
    private String googleCallback;
    private String googleAppName;

}
