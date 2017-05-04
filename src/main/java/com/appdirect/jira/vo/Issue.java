package com.appdirect.jira.vo;

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
    private Reporter reporter;
}
