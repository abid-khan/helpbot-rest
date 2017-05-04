package com.appdirect.jira.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by abidkhan on 20/04/17.
 */
@Builder
@Setter
@Getter
public class Project {
    private String key;
    private String name;
}
