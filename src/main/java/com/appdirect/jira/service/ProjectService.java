package com.appdirect.jira.service;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdirect.jira.helper.jira.JiraRestClientHelper;
import com.appdirect.jira.vo.Project;
import com.atlassian.jira.rest.client.domain.BasicProject;

/**
 * Created by abidkhan on 20/04/17.
 */
@Service
@Slf4j
public class ProjectService {

    @Autowired
    private JiraRestClientHelper jiraRestClientHelper;

    public List<Project> findAllProjects(String accessToken, String secret) {
        log.info("Fetching all projects");
        List<Project> projects =  new ArrayList<Project>();
       for (BasicProject basicProject : jiraRestClientHelper.jiraRestClient(accessToken,secret).getProjectClient().getAllProjects().claim()) {
            projects.add(Project.builder().key(basicProject.getKey()).name(basicProject.getName()).build());
        }
        return  projects;
    }
}
