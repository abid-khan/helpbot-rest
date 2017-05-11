package com.appdirect.jira.service;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.appdirect.jira.helper.jira.JiraRestClientHelper;
import com.appdirect.jira.props.Authentication;
import com.appdirect.jira.props.Url;
import com.appdirect.jira.vo.Issues;
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
@EnableConfigurationProperties({Url.class, Authentication.class})
public class IssueService {
    @Autowired
    private JiraRestClientHelper jiraRestClientHelper;
    @Autowired
    private Url url;
    @Autowired
    private Authentication authentication;

    public Issues findIssues(String query, String accessToken, String secret) {
        log.info("Fetching issues for query {}", query);
        JiraRestClient jiraRestClient = jiraRestClientHelper.jiraRestClient(accessToken, secret);
        List<com.appdirect.jira.vo.Issue> issues = new ArrayList<com.appdirect.jira.vo.Issue>();
        Promise<SearchResult> searchJqlPromise = jiraRestClient.getSearchClient().searchJql(query);
        for (BasicIssue basicIssue : searchJqlPromise.claim().getIssues()) {
            Issue issue = jiraRestClient.getIssueClient().getIssue(basicIssue.getKey()).claim();
            issues.add(com.appdirect.jira.vo.Issue.builder().url(buildIssueUrl(basicIssue.getKey())).key(issue.getKey()).summary(issue.getSummary()).reporter(Reporter.builder().name(issue.getReporter().getName()).build()).build());

        }
        return Issues.builder().issues(issues).build();
    }

    /**
     * @param key
     * @return
     */
    private String buildIssueUrl(String key) {
        return authentication.getBaseUrl().concat(url.getIssueUrl()).concat(key);
    }
}
