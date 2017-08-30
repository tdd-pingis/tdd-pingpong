package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class NavbarPage extends PageObject {

  public NavbarPage(RemoteWebDriver driver) {
    super(driver, "", "");
  }

  public NavbarPage(RemoteWebDriver driver, String title, String urlTail) {
    super(driver, title, urlTail);
  }

  public LoginPage navigateToLoginPage() {
    findLoginButton().click();
    return new LoginPage(driver);
  }

  public Dashboard navigateToDashboard() {
    findAccountButton().click();
    findDashboardButton().click();
    return new Dashboard(driver);
  }

  public Frontpage logout() {
    findAccountButton().click();
    findLogoutButton().click();
    return new Frontpage(driver);
  }

  public boolean currentUserIsAuthenticated() {
    findAccountButton();
    return true;
  }

  public boolean currentUserIsNotAuthenticated() {
    findLoginButton();
    return true;
  }

  private WebElement findLoginButton() {
    return findById("login-button");
  }

  private WebElement findAccountButton() {
    return findById("account-button");
  }

  private WebElement findLogoutButton() {
    return findById("logout-button");
  }

  private WebElement findDashboardButton() {
    return findById("dashboard-button");
  }
}
