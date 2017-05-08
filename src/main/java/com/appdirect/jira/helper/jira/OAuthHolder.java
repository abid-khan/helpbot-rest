package com.appdirect.jira.helper.jira;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by abidkhan on 07/05/17.
 */
@Builder
@Setter
@Getter
@ToString
public class OAuthHolder {
    public String token;
    public String verifier;
    public String secret;
}
