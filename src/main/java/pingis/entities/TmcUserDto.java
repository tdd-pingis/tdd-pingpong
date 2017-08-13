package pingis.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Implements Spring Security's OAuth2User and works only as a data transfer object between Spring
 * Security layer and UserService. This is then extracted in UserService and exported into database
 * as a TmcUser.
 *
 * @author villburn
 */
public class TmcUserDto implements OAuth2User {

  @NotNull
  @NotEmpty
  public String id;

  @NotNull
  @NotEmpty
  public String username;

  @NotNull
  @NotEmpty
  public String email;

  @NotNull
  @NotEmpty
  public boolean administrator;

  public TmcUserDto() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String roles = "USER";
    if (administrator) {
      roles += ",ADMIN";
    }
    return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
  }

  @Override
  public Map<String, Object> getAttributes() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("id", id);
    attributes.put("username", username);
    attributes.put("email", email);
    attributes.put("administrator", administrator);

    return attributes;
  }

  @Override
  public String toString() {
    return "User Details: "
        + "n\ttype: OAuthUser"
        + "\n\tid: " + id
        + "n\tname: " + username
        + "\n\tadministrator: " + administrator
        + "\n\temail: " + email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return username;
  }

  public void setName(String name) {
    this.username = name;
  }

  public void setUsername(String name) {
    this.username = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

}
