/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author juicyp
 */

@Configuration
@ConfigurationProperties(prefix = "security.oauth2.client.tmc")
public class OAuthProperties {
    
    private String clientAuthorizationMethod;
    private String authorizedGrantType;
    private String redirectURI;
    private String authorizationURI;
    private String tokenURI;
    private String userInfoURI;
    private String scopes;
    private String clientName;
    private String clientAlias;
    private String clientSecret = System.getenv("TMC_SECRET");
    
    public String getClientAuthorizationMethod() {
        return clientAuthorizationMethod;
    }

    public void setClientAuthorizationMethod(String clientAuthorizationMethod) {
        this.clientAuthorizationMethod = clientAuthorizationMethod;
    }

    public String getAuthorizedGrantType() {
        return authorizedGrantType;
    }

    public void setAuthorizedGrantType(String authorizedGrantType) {
        this.authorizedGrantType = authorizedGrantType;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public String getAuthorizationURI() {
        return authorizationURI;
    }

    public void setAuthorizationURI(String authorizationURI) {
        this.authorizationURI = authorizationURI;
    }

    public String getTokenURI() {
        return tokenURI;
    }

    public void setTokenURI(String tokenURI) {
        this.tokenURI = tokenURI;
    }

    public String getUserInfoURI() {
        return userInfoURI;
    }

    public void setUserInfoURI(String userInfoURI) {
        this.userInfoURI = userInfoURI;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAlias() {
        return clientAlias;
    }

    public void setClientAlias(String clientAlias) {
        this.clientAlias = clientAlias;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
}
