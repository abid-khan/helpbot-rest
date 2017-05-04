package com.appdirect.jira.props;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by abidkhan on 25/04/17.
 */
@ConfigurationProperties(prefix = "issue.query")
@Getter
@Setter
public class IssueQuery {
    private String myOpen;
    private String reportedByMe;
    private String iAmMentioned;
}
