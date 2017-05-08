package com.appdirect.jira.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.appdirect.jira.entity.JiraUser;

/**
 * Created by abidkhan on 08/05/17.
 */
public interface JiraUserRepository extends MongoRepository<JiraUser, String> {
    JiraUser findByUserId(String userId);
    JiraUser findByRequestToken(String requestToken);
}
