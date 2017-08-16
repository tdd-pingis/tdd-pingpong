/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.controllers;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author authority
 */
@Controller
public class FakeTmcController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping("/oauth/authorize")
  public String authorize(
          @RequestParam("state") String state,
          @RequestParam("redirect_uri") String redirectUri,
          RedirectAttributes redirectAttributes) {

    logger.debug("auth");
    redirectAttributes.addAttribute("code", "SplxlOBeZQQYbYS6WxSbIA");
    redirectAttributes.addAttribute("state", state);
    return "redirect:/oauth2/authorize/code/tmc";
  }

  @RequestMapping("/oauth/token")
  public ResponseEntity token(
          HttpServletResponse response,
          RedirectAttributes redirectAttributes) {

    logger.debug("token");
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

    FakeToken token = new FakeToken();
    token.setAccess_token("2YotnFZFEjr1zCsicMWpAA");
    token.setToken_type("bearer");

    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(token);
  }

  @RequestMapping("/api/v8/users/current")
  public ResponseEntity user() {

    logger.debug("user");
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

    FakeUser user = new FakeUser();
    user.setId("123");
    user.setUsername("fakeuser");
    user.setEmail("email");

    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(user);
  }
}

class FakeToken {

  private String access_token;
  private String token_type;

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getToken_type() {
    return token_type;
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
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
