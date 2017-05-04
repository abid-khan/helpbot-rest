package com.appdirect.jira.vo;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by abidkhan on 29/04/17.
 */
@Builder
@Setter
@Getter
public class Meetings {
    private List<Meeting> items;
}
