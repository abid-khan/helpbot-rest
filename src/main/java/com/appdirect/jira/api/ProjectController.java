package com.appdirect.jira.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appdirect.jira.service.ProjectService;

/**
 * Created by abidkhan on 21/04/17.
 */
@Component
@Path("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProjects(){
        return Response.ok().entity(projectService.findAllProjects()).type(MediaType.APPLICATION_JSON)
                .build();
    }
}
