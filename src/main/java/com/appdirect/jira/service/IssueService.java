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
import com.appdirect.jira.vo.Comment;
import com.appdirect.jira.vo.Field;
import com.appdirect.jira.vo.Issues;
import com.appdirect.jira.vo.Project;
import com.appdirect.jira.vo.User;
import com.appdirect.jira.vo.Version;
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

    /**
     * @param query
     * @param accessToken
     * @param secret
     * @return
     */
    public Issues findIssues(String query, String accessToken, String secret) {
        log.info("Fetching issues for query {}", query);
        JiraRestClient jiraRestClient = jiraRestClientHelper.jiraRestClient(accessToken, secret);
        List<com.appdirect.jira.vo.Issue> issues = new ArrayList<com.appdirect.jira.vo.Issue>();
        Promise<SearchResult> searchJqlPromise = jiraRestClient.getSearchClient().searchJql(query);
        for (BasicIssue basicIssue : searchJqlPromise.claim().getIssues()) {
            Issue issue = jiraRestClient.getIssueClient().getIssue(basicIssue.getKey()).claim();
            issues.add(com.appdirect.jira.vo.Issue.builder()
                    .url(buildIssueUrl(issue.getKey()))
                    .key(issue.getKey())
                    .summary(issue.getSummary())
                    .description(issue.getDescription())
                    .reporter(User.builder().name(issue.getReporter().getName()).displayName(issue.getReporter().getDisplayName()).build())
                    .project(Project.builder().name(issue.getProject().getName()).build())
                    .status(issue.getStatus().getName())
                    .resolution(issue.getResolution().getName())
                    .assignee(User.builder().name(issue.getAssignee().getName()).displayName(issue.getAssignee().getDisplayName()).build())
                    .priority(issue.getPriority().getName())
                    .createdDate(issue.getCreationDate().getMillis())
                    .dueDate(issue.getDueDate().getMillis())
                    .fixVersions(buildFixVersion(issue))
                    .affectedVersions(buildAffectedVersion(issue))
                    .fields(buildFields(issue))
                    .build());

        }
        return Issues.builder().issues(issues).build();
    }

    /**
     * @param taskId
     * @param accessToken
     * @param secret
     * @return issue detail
     */
    public com.appdirect.jira.vo.Issue findIssueDetail(String taskId, String accessToken, String secret) {
        log.info("Fetching detail for taskId {}", taskId);
        JiraRestClient jiraRestClient = jiraRestClientHelper.jiraRestClient(accessToken, secret);
        Issue issue = jiraRestClient.getIssueClient().getIssue(taskId).claim();
        com.appdirect.jira.vo.Issue issueVo = com.appdirect.jira.vo.Issue.builder()
                .url(buildIssueUrl(issue.getKey()))
                .key(issue.getKey())
                .summary(issue.getSummary())
                .description(issue.getDescription())
                .reporter(User.builder().name(issue.getReporter().getName()).displayName(issue.getReporter().getDisplayName()).build())
                .project(Project.builder().name(issue.getProject().getName()).build())
                .status(issue.getStatus().getName())
                .resolution(issue.getResolution().getName())
                .assignee(User.builder().name(issue.getAssignee().getName()).displayName(issue.getAssignee().getDisplayName()).build())
                .priority(issue.getPriority().getName())
                .createdDate(issue.getCreationDate().getMillis())
                .dueDate(issue.getDueDate().getMillis())
                .fixVersions(buildFixVersion(issue))
                .affectedVersions(buildAffectedVersion(issue))
                .fields(buildFields(issue))
                .build();


        return issueVo;
    }


    /**
     * @param issue
     * @return
     */
    private List<Version> buildFixVersion(Issue issue) {
        List<Version> fixedVersion = new ArrayList<>();
        for (com.atlassian.jira.rest.client.domain.Version version : issue.getFixVersions()) {
            fixedVersion.add(Version.builder().name(version.getName()).description(version.getDescription()).isArchieved(version.isArchived()).isReleased(version.isReleased()).build());
        }
        return fixedVersion;
    }

    /**
     * @param issue
     * @return
     */
    private List<Version> buildAffectedVersion(Issue issue) {
        List<Version> fixedVersion = new ArrayList<>();
        for (com.atlassian.jira.rest.client.domain.Version version : issue.getAffectedVersions()) {
            fixedVersion.add(Version.builder().name(version.getName()).description(version.getDescription()).isArchieved(version.isArchived()).isReleased(version.isReleased()).build());
        }
        return fixedVersion;
    }

    /**
     * @param issue
     * @return
     */
    private List<Field> buildFields(Issue issue) {
        List<Field> fields = new ArrayList<>();
        for (com.atlassian.jira.rest.client.domain.Field field : issue.getFields()) {
            fields.add(Field.builder().name(field.getName()).type(field.getType()).build());
        }
        return fields;
    }

    /**
     *
     * @param taskId
     * @param accessToken
     * @param secret
     * @return
     */
    public List<Comment> findComments(String taskId, String accessToken, String secret) {
        log.info("Fetching comments for taskId {}", taskId);
        JiraRestClient jiraRestClient = jiraRestClientHelper.jiraRestClient(accessToken, secret);
        Issue issue = jiraRestClient.getIssueClient().getIssue(taskId).claim();
        List<Comment> comments = new ArrayList<>();
        for (com.atlassian.jira.rest.client.domain.Comment comment : issue.getComments()) {
            comments.add(Comment.builder().body(comment.getBody()).auther(User.builder().name(comment.getAuthor().getName()).displayName(comment.getAuthor().getDisplayName()).build()).createdDate(comment.getCreationDate().getMillis()).updatedDate(comment.getUpdateDate().getMillis()).build());
        }
        return comments;
    }

    /**
     * @param key
     * @return
     */
    private String buildIssueUrl(String key) {
        return authentication.getBaseUrl().concat(url.getIssueUrl()).concat(key);
    }
}
