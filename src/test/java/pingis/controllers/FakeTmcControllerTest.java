package pingis.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pingis.config.OAuthProperties;
import pingis.config.SecurityConfig;

/**
 *
 * @author authority
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    FakeTmcController.class, SecurityConfig.class, OAuthProperties.class})

@WebAppConfiguration
@WebMvcTest(FakeTmcController.class)
public class FakeTmcControllerTest {

  @Autowired
  private MockMvc mvc;

  private final String fakeRedirectUri = "fakeuri";
  private final String fakeState = "fakestate";
  private final String testUser = "user";
  private final String testPass = "password";

  @Test
  public void fakeAuthorizeReturnsLoginPage() throws Exception {
    mvc.perform(get("/fake/authorize")
            .param("state", fakeState)
            .param("redirect_uri", fakeRedirectUri))
            .andExpect(status().isOk())
            .andExpect(view().name("fakelogin"));
  }

  @Test
  public void badCredentialsRedirectsToRedirectUri() throws Exception {
    badCredentialsPost()
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern(fakeRedirectUri + "*"));
  }

  @Test
  public void correctCredentialsRedirectsToRedirectUri() throws Exception {
    correctCredentialsPost()
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern(fakeRedirectUri + "*"));
  }

  @Test
  public void badCredentialsRedirectsWithAccessDeniedError() throws Exception {
    badCredentialsPost()
            .andExpect(model().attribute("error", "access_denied"));
  }

  @Test
  public void badCredentialsRetainsState() throws Exception {
    badCredentialsPost()
            .andExpect(model().attribute("state", fakeState));
  }

  @Test
  public void correctCredentialsRetainsState() throws Exception {
    correctCredentialsPost()
            .andExpect(model().attribute("state", fakeState));
  }

  @Test
  public void correctCredentialsRedirectsWithCode() throws Exception {
    correctCredentialsPost()
            .andExpect(model().attributeExists("code"));
  }

  @Test
  public void correctCredentialsRedirectsWithUsernameAsCode() throws Exception {
    correctCredentialsPost()
            .andExpect(model().attribute("code", testUser));
  }

  @Test
  public void cantGetToken() throws Exception {
    mvc.perform(get("/fake/token"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void tokenPostReturnsJson() throws Exception {
    mvc.perform(post("/fake/token")
            .param("code", "fakecode"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void tokenPostReturnsCodeAsToken() throws Exception {
    mvc.perform(post("/fake/token")
            .param("code", "fakecode"))
            .andExpect(content().json("{ \"access_token\": \"fakecode\" }"));
  }

  @Test
  public void tokenPostReturnsCorrectTokenType() throws Exception {
    mvc.perform(post("/fake/token")
            .param("code", "fakecode"))
            .andExpect(content().json("{ \"token_type\": \"bearer\" }"));
  }

  @Test
  public void cantPostUserinfo() throws Exception {
    mvc.perform(post("/fake/userinfo"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void userinfoWithoutAuthorizationReturnsBadRequest() throws Exception {
    mvc.perform(get("/fake/userinfo"))
            .andExpect(status().isBadRequest());
  }

  @Test
  public void badUserinfoRequestReturnsBadRequest() throws Exception {
    mvc.perform(get("/fake/userinfo")
            .header("Authorization", "Bearer fakeuser"))
            .andExpect(status().isBadRequest());
  }

  @Test
  public void correctUserinfoRequestReturnsOk() throws Exception {
    mvc.perform(get("/fake/userinfo")
            .header("Authorization", "Bearer user"))
            .andExpect(status().isOk());
  }

  @Test
  public void correctUserinfoRequestReturnsJson() throws Exception {
    mvc.perform(get("/fake/userinfo")
            .header("Authorization", "Bearer user"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void correctUserinfoRequestReturnsCorrectId() throws Exception {
    mvc.perform(get("/fake/userinfo")
            .header("Authorization", "Bearer user"))
            .andExpect(content().json("{ \"id\": \"1\" }"));
  }

  @Test
  public void correctUserinfoRequestReturnsCorrectUsername() throws Exception {
    mvc.perform(get("/fake/userinfo")
            .header("Authorization", "Bearer user"))
            .andExpect(content().json("{ \"username\": \"user\" }"));
  }

  @Test
  public void correctUserinfoRequestReturnsCorrectEmail() throws Exception {
    mvc.perform(get("/fake/userinfo")
            .header("Authorization", "Bearer user"))
            .andExpect(content().json("{ \"email\": \"email\" }"));
  }

  private ResultActions badCredentialsPost() throws Exception {
    return mvc.perform(post("/fake/authorize")
            .param("state", fakeState)
            .param("redirectUri", fakeRedirectUri)
            .param("username", "anyuser")
            .param("password", "anypass"));
  }

  private ResultActions correctCredentialsPost() throws Exception {
    return mvc.perform(post("/fake/authorize")
            .param("state", fakeState)
            .param("redirectUri", fakeRedirectUri)
            .param("username", testUser)
            .param("password", testPass));
  }
}
