package pingis.config;

import java.net.URI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationProperties;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import pingis.entities.User;

@Profile(value = {"prod", "oauth"})
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        // The TMC sandbox POSTs its results here and doesn't support authentication, so
        // skip authorization checks etc. for /submission-result.
        web.ignoring().antMatchers(HttpMethod.POST, "/submission-result");
    }

    @Autowired
    private OAuthProperties oauthProperties;
    private ClientRegistration tmcClientRegistration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().anyRequest().permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

        oauthLoginConfiguration(http);
    }

    private void oauthLoginConfiguration(HttpSecurity http) throws Exception {
        http.oauth2Login()
            .clients(tmcClientRegistration())
            .userInfoEndpoint().customUserType(User.class, URI.create(oauthProperties.getUserInfoUri()));
    }

    //Registers TMC's information for the application
    private ClientRegistration[] tmcClientRegistration() {
        ClientRegistrationProperties clientRegistrationProperties = new ClientRegistrationProperties();

        //copies all fields from the first object to the second
        BeanUtils.copyProperties(oauthProperties, clientRegistrationProperties);

        tmcClientRegistration = new ClientRegistration.Builder(clientRegistrationProperties).build();
        return new ClientRegistration[]{tmcClientRegistration};
    }
}