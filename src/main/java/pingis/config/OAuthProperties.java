package pingis.config;

import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * @author juicyp
 */

@Profile(value = {"prod", "oauth"})
@Configuration
@ConfigurationProperties(prefix = "security.oauth2.client.tmc")
public class OAuthProperties {

  @Value("${TMC_APP_ID:dummy}")
  private String clientId;
  @Value("${TMC_SECRET:dummy}")
  private String clientSecret;

  private ClientAuthenticationMethod clientAuthenticationMethod;
  private AuthorizationGrantType authorizedGrantType;
  private String redirectUri;
  private String authorizationUri;
  private String tokenUri;
  private String userInfoUri;
  private Set<String> scopes;
  private String clientName;
  private String clientAlias;
  private String userNameAttributeName;

  public ClientAuthenticationMethod getClientAuthenticationMethod() {
    return clientAuthenticationMethod;
  }

  public void setClientAuthenticationMethod(String clientAuthorizationMethod) {
    if (clientAuthorizationMethod.equals("basic")) {
      this.clientAuthenticationMethod = ClientAuthenticationMethod.BASIC;
    }
  }

  public AuthorizationGrantType getAuthorizedGrantType() {
    return authorizedGrantType;
  }

  public void setAuthorizedGrantType(String authorizedGrantType) {
    if (authorizedGrantType.equals("authorization_code")) {
      this.authorizedGrantType = AuthorizationGrantType.AUTHORIZATION_CODE;
    }
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getAuthorizationUri() {
    return authorizationUri;
  }

  public void setAuthorizationUri(String authorizationUri) {
    this.authorizationUri = authorizationUri;
  }

  public String getTokenUri() {
    return tokenUri;
  }

  public void setTokenUri(String tokenUri) {
    this.tokenUri = tokenUri;
  }

  public String getUserInfoUri() {
    return userInfoUri;
  }

  public void setUserInfoUri(String userInfoUri) {
    this.userInfoUri = userInfoUri;
  }

  public Set<String> getScopes() {
    return scopes;
  }

  public void setScopes(Set<String> scopes) {
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

  public String getUserNameAttributeName() {
    return userNameAttributeName;
  }

  public void setUserNameAttributeName(String userNameAttributeName) {
    this.userNameAttributeName = userNameAttributeName;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

}
