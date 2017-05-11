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
public class Field {
    private String name;
    private String type;
    private Object value;
}
