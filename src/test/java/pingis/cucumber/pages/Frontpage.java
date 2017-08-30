package pingis.cucumber.pages;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class Frontpage extends NavbarPage {

  public Frontpage(RemoteWebDriver driver) {
    super(driver, "#CodePong: Frontpage", "/");
  }
}
