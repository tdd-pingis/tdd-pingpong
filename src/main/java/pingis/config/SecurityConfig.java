package pingis.config;

import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationProperties;
import org.springframework.security.web.AuthenticationEntryPoint;
import pingis.entities.TmcUserDto;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void configure(WebSecurity web) throws Exception {
    // The TMC sandbox POSTs its results here and doesn't support authentication, so
    // skip authorization checks etc. for /submission-result.
    web.ignoring().antMatchers(HttpMethod.POST, "/submission-result");
  }

  @Autowired
  private OAuthProperties oauthProperties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
              .csrf().ignoringAntMatchers(
                        "/websocket/**",
                        "/oauth/authorize", "/oauth/token", "/api/v8/users/current")
              .and()
              .authorizeRequests().antMatchers(
                        "/css/**", "/webjars/**", "/", "/login", "/logout")
              .permitAll()
              .and()
              .authorizeRequests().antMatchers(
                        "/oauth/authorize", "/oauth/token", "/api/v8/users/current")
              .permitAll()
              .anyRequest().authenticated()
              .and()
              .exceptionHandling()
              .authenticationEntryPoint(new OAuthAuthenticationEntryPoint());

    oauthLoginConfiguration(http);
    oauth2LogoutConfiguration(http);
  }

  private void oauthLoginConfiguration(HttpSecurity http) throws Exception {
    http.oauth2Login()
              .clients(tmcClientRegistration())
              .userInfoEndpoint()
              .customUserType(TmcUserDto.class, URI.create(oauthProperties.getUserInfoUri()));
  }

  private void oauth2LogoutConfiguration(HttpSecurity http) throws Exception {
    http.logout()
              .logoutUrl("/logout")
              .logoutSuccessUrl("/");
  }

  //Registers TMC's information for the application
  private ClientRegistration tmcClientRegistration() {
    ClientRegistrationProperties clientRegistrationProperties = new ClientRegistrationProperties();

    //copies all fields from the first object to the second
    BeanUtils.copyProperties(oauthProperties, clientRegistrationProperties);

    return new ClientRegistration.Builder(clientRegistrationProperties).build();
  }

  class OAuthAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest hsr, HttpServletResponse hsr1,
              AuthenticationException ae)
              throws IOException, ServletException {
      hsr1.sendRedirect("/");
    }
  }
}
