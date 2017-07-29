package pingis.config;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationProperties;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuthProperties oauthProperties;
    private ClientRegistration tmcClientRegistration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                //temporary setup for testing OAuth2 authentication without a redirect url
                .antMatchers("/login").authenticated()
                .and()
            .oauth2Login()
                .clients(tmcClientRegistration());
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
