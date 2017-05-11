package com.appdirect.jira.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by abidkhan on 11/05/17.
 */
@Builder
@Setter
@Getter
public class Version {
    private String name;
    private String description;
    private boolean isReleased;
    private boolean isArchieved;
}
