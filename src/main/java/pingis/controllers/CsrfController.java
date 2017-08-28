package pingis.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CsrfController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping("/csrf")
  public CsrfToken csrf(CsrfToken token) {
    logger.debug("Returning CSRF token {}", token);
    return token;
  }
}
