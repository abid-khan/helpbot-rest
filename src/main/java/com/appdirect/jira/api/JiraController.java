package com.appdirect.jira.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    public Response getAllProjects(@QueryParam("userId") String userId){
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        return Response.ok().entity(projectService.findAllProjects(jiraUser.getAccessToken(), jiraUser.getSecret())).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/issues/myopen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyIssues(@QueryParam("userId") String userId) {
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        return Response.ok().entity(issueService.findMyOpenIssues(issueQuery.getMyOpen(), jiraUser.getAccessToken(), jiraUser.getSecret())).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/issues/reported-by-me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyReportedIssues(@QueryParam("userId") String userId) {
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        return Response.ok().entity(issueService.findMyOpenIssues(issueQuery.getReportedByMe(), jiraUser.getAccessToken(), jiraUser.getSecret())).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/issues/i-am-mentioned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMentionedIssues(@QueryParam("userId") String userId) {
        JiraUser jiraUser = jiraUserRepository.findByUserId(userId);
        return Response.ok().entity(issueService.findMyOpenIssues(issueQuery.getIAmMentioned(), jiraUser.getAccessToken(), jiraUser.getSecret())).type(MediaType.APPLICATION_JSON)
                .build();
    }

}
