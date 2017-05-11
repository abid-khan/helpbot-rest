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
public class Comment {
    private String body;
    private User auther;
    private Long createdDate;
    private Long updatedDate;
}
