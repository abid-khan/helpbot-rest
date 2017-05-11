package com.appdirect.jira.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.entity.JiraUser;
import com.appdirect.jira.helper.jira.JiraOAuthHelper;
import com.appdirect.jira.props.IssueQuery;
import com.appdirect.jira.repository.JiraUserRepository;
import com.appdirect.jira.service.IssueService;
import com.appdirect.jira.service.ProjectService;
import com.appdirect.jira.type.IssueType;
import com.appdirect.jira.vo.Comment;
import com.appdirect.jira.vo.Issue;
import com.appdirect.jira.vo.Issues;

/**
 * Created by abidkhan on 25/04/17.
 */
@Component
@Path("/jira")
@EnableConfigurationProperties(IssueQuery.class)
@Slf4j
public class JiraController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueQuery issueQuery;

    @Autowired
    private JiraOAuthHelper jiraOAuthHelper;

    @Autowired
    private JiraUserRepository jiraUserRepository;

    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProjects(@QueryParam("userId") String userId) {
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        return Response.ok().entity(projectService.findAllProjects(jiraUser.getAccessToken(), jiraUser.getSecret())).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/issues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyIssues(@QueryParam("userId") String userId, @QueryParam("type") String type) {
        Issues issues = null;
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        if (IssueType.MYOPEN.getType().equals(type)) {
            issues = issueService.findIssues(issueQuery.getMyOpen(), jiraUser.getAccessToken(), jiraUser.getSecret());
        } else if (IssueType.IAMMENTIONED.getType().equals(type)) {
            issues = issueService.findIssues(issueQuery.getIAmMentioned(), jiraUser.getAccessToken(), jiraUser.getSecret());
        } else if (IssueType.REPORTEDBYME.getType().equals(type)) {
            issues = issueService.findIssues(issueQuery.getReportedByMe(), jiraUser.getAccessToken(), jiraUser.getSecret());

        } else {
            issues = issueService.findIssues(issueQuery.getMyOpen(), jiraUser.getAccessToken(), jiraUser.getSecret());
        }
        return Response.ok().entity(issues).type(MediaType.APPLICATION_JSON)
                .build();
    }


    @GET
    @Path("/issues/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIssueDetail(@QueryParam("userId") String userId, @PathParam("id") String jiraId) {
        log.info("Request received to get details of jiraId {} fo userId", jiraId, userId);
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        Issue issue = issueService.findIssueDetail(jiraId, jiraUser.getAccessToken(), jiraUser.getSecret());
        return Response.ok().entity(issue).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/issues/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@QueryParam("userId") String userId, @PathParam("id") String jiraId) {
        log.info("Request received to get comments of jiraId {} fo userId", jiraId, userId);
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        List<Comment> comments = issueService.findComments(jiraId, jiraUser.getAccessToken(), jiraUser.getSecret());
        return Response.ok().entity(comments).type(MediaType.APPLICATION_JSON)
                .build();
    }

}
