package com.appdirect.jira.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by abidkhan on 08/05/17.
 */
@Document(collection = "jirauser")
@Builder
@Setter
@Getter
public class JiraUser {
    @Id
    private String id;
    private String userId;
    private String requestToken;
    private String secret;
    private String accessToken;
    private String verifirer;
}
