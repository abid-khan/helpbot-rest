package com.appdirect.jira.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.props.IssueQuery;
import com.appdirect.jira.service.IssueService;

/**
 * Created by abidkhan on 25/04/17.
 */
@Component
@Path("/issues")
@EnableConfigurationProperties(IssueQuery.class)
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueQuery issueQuery;

    @GET
    @Path("/myopen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyIssues(){
        return Response.ok().entity(issueService.findMyOpenIssues(issueQuery.getMyOpen())).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/reported-by-me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyReportedIssues(){
        return Response.ok().entity(issueService.findMyOpenIssues(issueQuery.getReportedByMe())).type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/i-am-mentioned")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMentionedIssues(){
        return Response.ok().entity(issueService.findMyOpenIssues(issueQuery.getIAmMentioned())).type(MediaType.APPLICATION_JSON)
                .build();
    }

}
