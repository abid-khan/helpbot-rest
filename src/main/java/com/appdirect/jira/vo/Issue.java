package com.appdirect.jira.vo;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by abidkhan on 25/04/17.
 */
@Builder
@Setter
@Getter
public class Issue {
    private String url;
    private String key;
    private String summary;
    private String description;
    private Project project;
    private String status;
    private String resolution;
    private User reporter;
    private User assignee;
    private String priority;
    private Long createdDate;
    private Long dueDate;
    private List<Version> fixVersions;
    private List<Version> affectedVersions;
    private List<Field> fields;
}
