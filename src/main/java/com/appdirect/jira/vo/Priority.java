package com.appdirect.jira.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import groovy.transform.builder.Builder;

/**
 * Created by arvindkasale on 5/10/17.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Priority {
	private String name;
}
