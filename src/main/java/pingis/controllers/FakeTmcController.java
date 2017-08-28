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

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
      logger.debug("Access denied");
      redirectAttributes.addAttribute("error", "access_denied");
    } else {
      logger.debug("Access granted");
      redirectAttributes.addAttribute("code", username);
    }

    redirectAttributes.addAttribute("state", state);
    String redirectPath = redirectUri.replace("http://localhots:8080", "");

    logger.debug("Redirecting to {}", redirectPath);
    return "redirect:" + redirectPath;
  }

  @RequestMapping(value = "/fake/token", method = RequestMethod.POST)
  public ResponseEntity<FakeToken> token(
          @RequestParam("code") String code,
          HttpServletRequest request,
          HttpServletResponse response) {

    logger.debug("Fake TMC controller received an access token request with code {}", code);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    FakeToken fakeToken = new FakeToken();
    fakeToken.accessToken = code;
    fakeToken.tokenType = "bearer";

    logger.debug("Sent token {}", fakeToken.accessToken);

    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(fakeToken);
  }

  @RequestMapping(value = "/fake/userinfo", method = RequestMethod.GET)
  public ResponseEntity<FakeUser> user(
          HttpServletRequest request) {
    logger.debug("Received userinfo request");

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
            "Fake TMC controller received a userinfo request with authorization {}", authorization);

    if (!ids.containsKey(token)) {
      logger.debug("Invalid token");

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

    FakeUser user = new FakeUser();
    user.id = ids.get(token);
    user.username = token;
    user.email = "email";

    logger.debug("Set the user id as {}", user.id);

    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(user);
  }

  class FakeToken {

    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("token_type")
    public String tokenType;
  }

  class FakeUser {

    public String id;
    public String username;
    public String email;
  }
}
