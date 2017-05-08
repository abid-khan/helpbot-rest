package com.appdirect.jira.vo;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by abidkhan on 08/05/17.
 */
@Builder
@Setter
@Getter
public class Issues {
    private String oAuthUrl;
    private List<Issue> issues;
}
