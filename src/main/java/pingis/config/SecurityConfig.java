package pingis.config;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationProperties;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuthProperties oauthProperties;
    private ClientRegistration tmcClientRegistration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
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
