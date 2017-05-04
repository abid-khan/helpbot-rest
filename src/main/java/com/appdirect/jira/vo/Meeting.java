package com.appdirect.jira.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by abidkhan on 29/04/17.
 */
@Builder
@Setter
@Getter
public class Meeting {
    private String summary;
    private Long startTime;
}
