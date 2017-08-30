package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class LoginPage extends NavbarPage {

  public LoginPage(RemoteWebDriver driver) {
    super(driver, "#CodePong: Fake-login", "/fake/authorize");
  }

  public Dashboard loginSuccessfully(String username, String password) {
    tryLogin(username, password);
    return new Dashboard(driver);
  }

  public LoginPage loginUnsuccessfully(String username, String password) {
    tryLogin(username, password);
    return new LoginPage(driver);
  }

  private void tryLogin(String username, String password) {
    usernameField().sendKeys(username);
    passwordField().sendKeys(password);
    findLoginButton().click();
  }

  public WebElement usernameField() {
    return findById("session_login");
  }

  public WebElement passwordField() {
    return findById("session_password");
  }

  public WebElement findLoginButton() {
    return findById("log-in-button");
  }
}
