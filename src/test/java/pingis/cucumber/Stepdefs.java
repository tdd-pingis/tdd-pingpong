package pingis.cucumber;


import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pingis.services.DataImporter;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration
public class Stepdefs {

  private static final int DRIVER_WAIT_TIME = 4;
  private static final int LOAD_WAIT_TIME = 1000;

  WebDriver driver;
  String baseUrl;
  
  @Autowired
  DataImporter dataImporter;
  
  // Maps the names for the elements and contents used in the feature files to
  // actual ids or contents that can be used by the WebDriver to locate them
  MultiValueMap<String, String>ids;
  
  Map<String, String> testUrls;
  
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
    dataImporter.dropDatabase();
    dataImporter.initializeDatabase();
    ids = initializeIds();
    testUrls = initializeTestUrls();
  }
  
  private WebElement findElementByName(String name) {
    return driver.findElement(By.id(ids.getFirst(name)));
  }

  private boolean exists(String name) {
    return ids.get(name).stream()
            .allMatch(e -> existsElementId(e));
  }
  
  private boolean existsElementId(String id) {
    return driver.findElements(By.id(id)).size() > 0;
  }

  private boolean isDisplayed(String name) {
    // Check that all the contents and elements required can be found
    return ids.get(name).stream()
            .allMatch(e -> driver.getPageSource().contains(e) || existsElementId(e));
  }
  
  @When(".*clicks the (.+) button$")
  public void clicks_the_button(String name) throws InterruptedException {
    findElementByName(name).click();
  }

  @When(".*clicks the (.+) tab$")
  public void clicks_the_tab(String name) throws InterruptedException {
    driver.findElement(By.linkText(name)).click();
    //Wait for the tab contents to load.
    //The implicit wait doesn't work as the tab content checking is
    //currently done by using the page source instead of an actual element
    Thread.sleep(LOAD_WAIT_TIME);
  }
  
  @When(".*inputs and submits data for new Task pair")
  public void inputs_and_submits_new_task_pair() 
          throws InterruptedException {
    
    findElementByName("test task description").sendKeys("impl task desc");
    findElementByName("test task name").sendKeys("impl task name");
    findElementByName("implementation task description").sendKeys("impl task desc");
    findElementByName("implementation task name").sendKeys("impl task desc");
    findElementByName("test task codestub").sendKeys("public class KalkulaattoriTest "
            + "{\n\tpublic Kalkulaattori {}\n}");
    findElementByName("implementation task codestub").sendKeys("public class Kalkulaattori "
            + "{\n\tpublic KalkulaattoriTest {}\n}");
    clicks_the_button("submit");
  }


  @And(".*inputs and submits the challenge description")
  public void inputs_and_submits_live_challenge_description() throws InterruptedException {
    findElementByName("challenge name").sendKeys("Kalkulaattori");
    findElementByName("challenge description").sendKeys("In this challenge you will create "
                                                  + "a perfect Kalkulaattori.");
    clicks_the_button("submit");
  }

  @When(".*wants to participate in Live challenge")
  public void wants_to_participate_in_live_challenge() throws InterruptedException {
    clicks_the_button("create live challenge");
  }

  @When(".*inputs their username (.+) and password (.+)")
  public void inputs_username_and_password(String username, String password) throws Throwable {
    findElementByName("username field").sendKeys(username);
    findElementByName("password field").sendKeys(password);
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
 
  @Given("(.+) is logged in$")
  public void is_logged_in(String user) throws Throwable {
    clicks_the_button("Login");
    String username = user.toLowerCase();
    inputs_username_and_password(username, "password");
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

  @Given(".*has chosen to embark on a new challenge")
  public void navigates_to_the_task_page_at() throws Throwable {
    findElementByName("Calculator challenge").click();
    findElementByName("Test task multiply").click();
  }

  @Then(".*the page contains (.+)")
  public void page_has_the_right_content(String content) throws Throwable {
    assertTrue(driver.getPageSource().contains(content));
  }
  
  @Then(".*the (.+) is shown")
  public void page_is_shown(String page) {
    String currentUrl = driver.getCurrentUrl();
    String message = "wrong url, found: " + driver.getCurrentUrl() 
                   + ",  \n expected " + testUrls.get(page);
    
    assertTrue(message, driver.getCurrentUrl().equals(testUrls.get(page)));
    assertTrue(isDisplayed(page));
  }
  
  @And(".*has successfully submitted new challenge with first task pair")
  public void has_successfully_submitted_new_challenge_with_first_task_pair() 
                                                throws InterruptedException {
    wants_to_participate_in_live_challenge();
    inputs_and_submits_live_challenge_description();
    inputs_and_submits_new_task_pair();
  }
  
  @When(".*inputs and submits (.+) code")
  public void inputs_and_submits_code_to_editor(String codetype) throws InterruptedException {
    if (codetype.equals("test")) {
      driver.findElement(By.id("submission-editor")).click();
      driver.findElement(By.id("submission-editor")).sendKeys(Keys.CONTROL, "a");
      driver.findElement(By.id("submission-editor")).sendKeys(
                        "import org.junit.Test;\n",
                        "import static org.junit.Assert.*;\n",
                        "import fi.helsinki.cs.tmc.edutestutils.Points;\n\n",
                        "@Points(\"03-03\")\n",
                        "public class CalculatorTest {\n\n",
                        "    @Test\n",
                        "    public void testAddition() {\n",
                        "        Calculator calc = new Calculator();\n",
                        "        assertEquals(5, calc.add(3,2)\n",
                        "    }\n",
                        "}\n");
      
    } else if (codetype.equals("implementation")) {
      driver.findElement(By.id("submission-editor")).click();
      driver.findElement(By.id("submission-editor")).sendKeys(Keys.CONTROL, "a");
      driver.findElement(By.id("submission-editor")).sendKeys(
                        "public class Calculator {\n\n",
                        "    public void add(int x, int y) {\n",
                        "        return x+y;\n",
                        "    }\n",
                        "}\n");
    } 
    
    clicks_the_button("submit");
    checkAlert();
    Thread.sleep(LOAD_WAIT_TIME);
  }
  
  public void checkAlert() {
    try {
      WebDriverWait wait = new WebDriverWait(driver, 2);
      wait.until(ExpectedConditions.alertIsPresent());
      Alert alert = driver.switchTo().alert();
      alert.accept();
    } catch (Exception e) {
      assertFalse(e.getMessage(), true);
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
  }

  private MultiValueMap<String, String> initializeIds() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    map.add("Login", "login-button");
    map.add("submit", "submit-button");
    map.add("My Account", "account-button");
    map.add("Log in", "log-in-button");
    map.add("Logout", "logout-button");
    map.add("user page", "user");
    map.add("front page", "");
    map.add("task page", "task");
    map.add("editor page", "Level:");
    map.add("feedback page", "results");
    map.add("login error page", "login?error");
    map.add("username field", "session_login");
    map.add("password field", "session_password");
    map.add("Calculator challenge", "open-challenge-Calculator");
    map.add("Dashboard", "progresscircle");
    map.add("challenge name", "challenge_name");
    map.add("Test task multiply", "test-task-1");
    map.add("challenge description", "challenge_desc");
    map.add("create live challenge", "create_live_challenge");
    map.add("join live challenge", "join_live_challenge");
    map.add("continue live challenge", "continue_live_challenge");
    map.add("implementation task name", "impl-task-name-input");
    map.add("implementation task description", "impl-task-desc-input");
    map.add("test task name", "test-task-name-input");
    map.add("test task description", "test-task-desc-input");
    map.add("test task codestub", "test-task-code-stub");
    map.add("implementation task codestub", "impl-task-code-stub");
    map.add("implement next task page", "next-task-page");
    map.add("implement next task page", "Challenge");
    map.add("new task pair", "test-task-desc-input");
    map.add("new task pair", "test-task-desc-label");
    map.add("new task pair", "New task pair");
    map.add("new task pair", "impl-task-name-input");
    map.add("submission code", "submission-code");

    return map;
  }

  private Map<String, String> initializeTestUrls() {
    Map<String, String> urls = new HashMap<>();
    
    urls.put("user page", "/user");
    urls.put("feedback page", "/feedback");
    urls.put("implement next task page", "/task");
    urls.put("error page", "/error");
    urls.put("new challenge page", "/newchallenge");
    urls.put("new task pair", "/createTaskPair");
    urls.put("front page", "");
    
    return urls;
  }
}