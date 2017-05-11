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
import com.appdirect.jira.type.IssueType;
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

        }
        return Response.ok().entity(issues).type(MediaType.APPLICATION_JSON)
                .build();
    }

}
