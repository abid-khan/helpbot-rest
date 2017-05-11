package com.appdirect.jira.type;

import lombok.Getter;

/**
 * Created by abidkhan on 11/05/17.
 */
@Getter
public enum IssueType {
    IAMMENTIONED("ihavebeenmentioned"), MYOPEN("iamassigned"), REPORTEDBYME("reportedbyme");

    IssueType(String type) {
        this.type = type;
    }

    private String type;
}
