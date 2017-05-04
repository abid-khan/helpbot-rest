package com.appdirect.jira.props;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by abidkhan on 26/04/17.
 */
@ConfigurationProperties(prefix = "url.prefix")
@Getter
@Setter
public class Url {
    private String issueUrl;
}
