package com.appdirect.jira.helper.jira;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.appdirect.jira.props.Authentication;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.signature.RSA_SHA1;

/**
 * Created by abidkhan on 08/05/17.
 */
@Component
@EnableConfigurationProperties(Authentication.class)
public class OAuthAccessorHelper {
    @Autowired
    private Authentication authentication;

    /**
     * @return <code>OAuthAccessor</code>
     * @throws Exception
     */
    public OAuthAccessor oAuthAccessor() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        OAuthServiceProvider serviceProvider = new OAuthServiceProvider(authentication.getBaseUrl() + authentication.getRequestTokenUrl(), authentication.getBaseUrl() + authentication.getAuthorizationUrl(), authentication.getBaseUrl() + authentication.getAccessTokenUrl());
        OAuthConsumer consumer = new OAuthConsumer(authentication.getCallbackUrl(), authentication.getConsumerKey(), authentication.getConsumerSecret(), serviceProvider);
        consumer.setProperty(RSA_SHA1.PRIVATE_KEY, authentication.getPrivateKey());
        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
        //consumer.setProperty("xoauth_requestor_id", "abid.cse2007@gmail.com");
        return new OAuthAccessor(consumer);
    }
}
