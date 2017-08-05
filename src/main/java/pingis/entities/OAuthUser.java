package pingis.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuthUser implements OAuth2User {
    
    public String id;
    public String username;
    public String email;
    public boolean administrator;
    
    public OAuthUser() {}
    
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
