package pingis.cucumber;

import static org.junit.Assert.assertTrue;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration
public class Stepdefs {

  private static final int DRIVER_WAIT_TIME = 4;
  private static final int LOAD_WAIT_TIME = 1000;

  WebDriver driver;
  String baseUrl;
  //Maps the names for the elements used in the feature files to
  //actual ids or addresses that can be used by the WebDriver to locate them
  Map<String, String> ids;

  @Test
  public void setupTest() {
  }

  @Before
  public void setUp() throws Exception {
    if (driver == null) {
      driver = new FirefoxDriver();
      driver.manage().deleteAllCookies();
      //Waits the specified amount of time if it cannot immediately find a desired element
      driver.manage().timeouts().implicitlyWait(DRIVER_WAIT_TIME, TimeUnit.SECONDS);
    }
    baseUrl = "http://localhost:8080/";
    driver.get(baseUrl);

    ids = initializeIds();
  }

  private WebElement findByName(String name) {
    return driver.findElement(By.id(ids.get(name)));
  }

  private boolean exists(String name) {
    return driver.findElements(By.id(ids.get(name))).size() > 0;
  }

  private boolean isDisplayed(String name) {
    return findByName(name).isDisplayed();
  }

  @When(".*clicks the (.+) button$")
  public void clicks_the_button(String name) throws InterruptedException {
    findByName(name).click();
  }

  @When(".*clicks the (.+) tab$")
  public void clicks_the_tab(String name) throws InterruptedException {
    driver.findElement(By.linkText(name)).click();
    //Wait for the tab contents to load.
    //The implicit wait doesn't work as the tab content checking is
    //currently done by using the page source instead of an actual element
    Thread.sleep(LOAD_WAIT_TIME);
  }

  @When(".*inputs their username (.+) and password (.+)")
  public void inputs_username_and_password(String username, String password) throws Throwable {
    findByName("username field").sendKeys(username);
    findByName("password field").sendKeys(password);
  }

  @Then(".*is successfully authenticated$")
  public void is_successfully_authenticated() throws Throwable {
    assertTrue(exists("My Account"));
    assertTrue(isDisplayed("My Account"));
  }

  @And(".*is redirected to the login page")
  public void is_redirected_to_login_page() {
    assertTrue(exists("username field"));
  }

  @And(".*is redirected to the user page")
  public void is_redirected_to_user_page() {
    assertTrue(exists("Dashboard"));
  }

  @Given(".*is logged in$")
  public void is_logged_in() throws Throwable {
    clicks_the_button("Login");
    inputs_username_and_password("user", "password");
    clicks_the_button("Log in");
  }

  @Then(".*is successfully signed out$")
  public void successfully_signed_out() throws Throwable {
    not_authenticated();
  }

  @Then(".*is not authenticated$")
  public void not_authenticated() throws Throwable {
    assertTrue(exists("Login"));
  }

  @And("clicks Sign in")
  public void clicks_on_sign_in() throws Throwable {
    driver.findElement(By.name("commit")).click();
  }

  @Given(".*navigates to the task page at (.+)")
  public void navigates_to_the_task_page_at(String address) throws Throwable {
    driver.get(baseUrl + address);
  }

  @Then(".*the page contains (.+)")
  public void page_has_the_right_content(String content) throws Throwable {
    assertTrue(driver.getPageSource().contains(content));
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  private Map<String, String> initializeIds() {
    Map<String, String> map = new HashMap<>();

    map.put("Login", "login-button");
    map.put("submit", "submit-button");
    map.put("My Account", "account-button");
    map.put("Log in", "log-in-button");
    map.put("Logout", "logout-button");
    map.put("user page", "user");
    map.put("front page", "");
    map.put("login error page", "login?error");
    map.put("username field", "session_login");
    map.put("password field", "session_password");
    map.put("Dashboard", "progresscircle");

    return map;
  }
}
