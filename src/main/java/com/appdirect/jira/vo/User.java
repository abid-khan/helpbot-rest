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
public class User {
    private String name;
    private String displayName;
}
