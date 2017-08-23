package pingis.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author authority
 */
@Profile("dev")
@Controller
public class FakeTmcController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping(value = "/fake/authorize", method = RequestMethod.GET)
  public String login(
          @RequestParam("state") String state,
          @RequestParam("redirect_uri") String redirectUri,
          Model model) {

    logger.debug("Fake TMC controller received an authorization request");
    logger.debug("Redirecting to the login page");

    model.addAttribute("state", state);
    model.addAttribute("redirectUri", redirectUri);
    return "fakelogin";
  }

  @RequestMapping(value = "/fake/authorize", method = RequestMethod.POST)
  public String authorize(
          @RequestParam("state") String state,
          @RequestParam("redirectUri") String redirectUri,
          @RequestParam("username") String username,
          @RequestParam("password") String password,
          RedirectAttributes redirectAttributes) {

    logger.debug("Fake TMC controller received login credentials");

    //TODO: Move the test users to a centralized place from here and DataImporter
    Map<String, String> creds = new HashMap<>();
    creds.put("user", "password");
    creds.put("admin", "password");
    creds.put("impluser", "password");

    if (!creds.containsKey(username) || !creds.get(username).equals(password)) {
      redirectAttributes.addAttribute("error", "access_denied");
    } else {
      redirectAttributes.addAttribute("code", username);
    }

    redirectAttributes.addAttribute("state", state);
    String redirectPath = redirectUri.replace("http://localhots:8080", "");

    return "redirect:" + redirectPath;
  }

  @RequestMapping(value = "/fake/token", method = RequestMethod.POST)
  public ResponseEntity<FakeToken> token(
          @RequestParam("code") String code,
          HttpServletRequest request,
          HttpServletResponse response) {

    logger.debug("Fake TMC controller received an access token request with code " + code);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    FakeToken fakeToken = new FakeToken();
    fakeToken.setAccessToken(code);
    fakeToken.setTokenType("bearer");

    logger.debug("Sent token " + fakeToken.getAccessToken());

    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(fakeToken);
  }

  @RequestMapping(value = "/fake/userinfo", method = RequestMethod.GET)
  public ResponseEntity<FakeUser> user(
          HttpServletRequest request) {

    Map<String, String> ids = new HashMap<>();
    ids.put("admin", "0");
    ids.put("user", "1");
    ids.put("impluser", "2");

    String authorization = request.getHeader("Authorization");
    
    if (authorization == null) {
      logger.debug("Invalid headers");
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    
    String token = authorization.replace("Bearer ", "");

    logger.debug(
            "Fake TMC controller received a userinfo request with authorization " + authorization);

    if (!ids.containsKey(token)) {
      logger.debug("Invalid token");
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

    FakeUser user = new FakeUser();
    user.setId(ids.get(token));
    user.setUsername(token);
    user.setEmail("email");

    logger.debug("Set the user id as " + user.getId());

    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(user);
  }

  class FakeToken {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;

    public String getAccessToken() {
      return accessToken;
    }

    public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
    }

    public String getTokenType() {
      return tokenType;
    }

    public void setTokenType(String tokenType) {
      this.tokenType = tokenType;
    }
  }

  class FakeUser {

    private String id;
    private String username;
    private String email;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }
}
