package com.appdirect.jira.service;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.appdirect.jira.props.Url;
import com.appdirect.jira.vo.Reporter;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.util.concurrent.Promise;

/**
 * Created by abidkhan on 25/04/17.
 */
@Service
@Slf4j
@EnableConfigurationProperties(Url.class)
public class IssueService {
    @Autowired
    private JiraRestClient jiraRestClient;

    @Autowired
    private Url url;

    public List<com.appdirect.jira.vo.Issue> findMyOpenIssues(String query) {
        log.info("Fetching issues for query {}", query);
        List<com.appdirect.jira.vo.Issue> issues = new ArrayList<com.appdirect.jira.vo.Issue>();
        Promise<SearchResult> searchJqlPromise = jiraRestClient.getSearchClient().searchJql(query);
        for (BasicIssue basicIssue : searchJqlPromise.claim().getIssues()) {
            Issue issue = jiraRestClient.getIssueClient().getIssue(basicIssue.getKey()).claim();
            issues.add(com.appdirect.jira.vo.Issue.builder().url(url.getIssueUrl().concat(basicIssue.getKey())).key(issue.getKey()).summary(issue.getSummary()).reporter(Reporter.builder().name(issue.getReporter().getName()).build()).build());

        }
        return issues;
    }
}
