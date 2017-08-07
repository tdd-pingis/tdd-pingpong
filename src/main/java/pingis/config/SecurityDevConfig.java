package pingis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("dev")
@EnableWebSecurity
public class SecurityDevConfig extends WebSecurityConfigurerAdapter {
    
    // TODO: Setup "mock" for dev environment, remove from this configuration
    @Override
    public void configure(WebSecurity web) throws Exception {
        // The TMC sandbox POSTs its results here and doesn't support authentication, so
        // skip authorization checks etc. for /submission-result.
        web.ignoring().antMatchers(HttpMethod.POST, "/submission-result");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().ignoringAntMatchers("/websocket/**")
            .and()
            .authorizeRequests().anyRequest().permitAll()
            .and()
            .formLogin().loginPage("/login");
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN");
    }
}
