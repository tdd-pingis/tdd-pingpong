package pingis.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pingis.services.DataImporter;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration
public class Stepdefs {
          
  private static final int DRIVER_WAIT_TIME = 4;
  private static final int LOAD_WAIT_TIME = 1000;
  private static final String baseUrl = "http://localhost:8080/";

  WebDriver driver;
  
  @Autowired
  DataImporter dataImporter;
  
  // Maps the names for the elements and contents used in the feature files to
  // actual ids or contents; can be used by the WebDriver in order to locate them in the page
  // Note: You can add multiple values under one key, this works nicely for checking that multiple
  // elements exist at the same page simultaneously (@Then-steps), however when searching for 
  // on action elements (@When-steps), searches for the first of the list (adviceable to use only
  // one value for @When-steps) - villburn
  MultiValueMap<String, String> ids;
  
  Map<String, String> testUrls; // In order to verify that urls are correct
  Map<String, String> inputs;   // Contains dummy inputs made by the WebDriver 
  
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
    
    dataImporter.dropDatabase();
    dataImporter.initializeDatabase();
    inputs = initializeInputs();
    ids = initializeIds();
    testUrls = initializeTestUrls();

    navigateToBaseUrl();
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
    // check special case, user should not be able to re-join his own challenge: 
    // -> somewhat bad design, here for convenience
    // TODO: refactor
    if (name.equals("join live challenge") && !exists(name)) { 
      assertTrue(exists("create live challenge") || exists("continue live challenge"));
    } else {
      findElementByName(name).click();
    }
  }
  
  @When(".*clicks the (.+) tab$")
  public void clicks_the_tab(String name) throws InterruptedException {
    driver.findElement(By.linkText(name)).click();
    //Wait for the tab contents to load.
    //The implicit wait doesn't work as the tab content checking is
    //currently done by using the page source instead of an actual element
    Thread.sleep(LOAD_WAIT_TIME);
  }
  
  @And(".*inputs and submits the challenge description")
  public void inputs_and_submits_live_challenge_description() throws InterruptedException {
    findElementByName("challenge name").sendKeys(inputs.get("challenge 1 name"));
    findElementByName("challenge description").sendKeys(inputs.get("challenge 1 description"));
    clicks_the_button("submit");
  }

  @When("(.+) inputs and submits (.+) code$")
  public void user_inputs_and_submits_test_code(String user, String codeType) throws Throwable {
    inputs_and_submits_code_to_editor(user, codeType, "first");
    if (!codeType.equals("empty")) {
      waitForFeedbackSuccessMessage();
    }
  }
  
  @When(".*inputs and submits data for (.+) task pair")
  public void inputs_and_submits_new_task_pair(String howMany) 
          throws InterruptedException {
    
    findElementByName("test task name")
            .sendKeys(inputs.get(howMany + " test task name"));
    findElementByName("test task description")
            .sendKeys(inputs.get(howMany + " test task description"));
    findElementByName("implementation task description")
            .sendKeys(inputs.get(howMany + " implementation task description"));
    findElementByName("implementation task name")
            .sendKeys(inputs.get(howMany + " implementation task name"));
    findElementByName("test task codestub")
            .sendKeys(inputs.get(howMany + " test task codestub"));
    findElementByName("implementation task codestub")
            .sendKeys(inputs.get(howMany + " implementation task codestub"));
    
    clicks_the_button("submit");
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
  
  @And("(.+) wants to (.+) Live challenge")
  public void wants_to_add_verb_here_live_challenge(String user, String verb) throws Throwable {
    if (verb.equals("participate in")) {
      clicks_the_button("create live challenge");
      
    } else if (verb.equals("join in") || verb.equals("continue")) {
      beginNewSession();
      navigateToBaseUrl();
      is_logged_in(user);
    }
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
  public void page_is_shown(String page) throws InterruptedException {
    String currentUrl = driver.getCurrentUrl();
    String message = "wrong url, found: " + driver.getCurrentUrl() 
                   + ",  \n expected to contain " + testUrls.get(page);
    
    assertTrue(message, driver.getCurrentUrl().contains(testUrls.get(page)));
    assertTrue(isDisplayed(page));
  }
  
  @And("(.+) has successfully submitted new challenge with first task pair")
  public void has_successfully_submitted_new_challenge_with_first_task_pair(String user) 
                                                throws InterruptedException, Throwable {
    wants_to_add_verb_here_live_challenge(user, "participate in");
    inputs_and_submits_live_challenge_description();
    inputs_and_submits_new_task_pair("first");
  }
  
  @And("(.+) has successfully submitted (.+) code for (.+) task pair")
  public void has_successfully_submitted_test_code_for_first_task(String user, String codetype, 
                                                                  String howMany) 
                                                throws InterruptedException {
    inputs_and_submits_code_to_editor(user, codetype, howMany);
    waitForFeedbackSuccessMessage();
  }

  private void waitForFeedbackSuccessMessage() throws InterruptedException {
    WebElement myDynamicElement = (new WebDriverWait(driver, 12))
            .until(ExpectedConditions.presenceOfElementLocated(By.id("results-panel")));
  }

  @And("page (.+) the (.+) input by (.+)")
  public void page_contains_the_code_input_by(String contains, String codeType, String user) {
    boolean isContains = contains.equals("contains"); 
    assertEquals(isContains, isDisplayed(codeType + " input by " + user));
  }
  
  @When("(.+) inputs and submits (.+) code to (.+) task pair")
  public void inputs_and_submits_code_to_editor(String user, String codetype, String howMany) 
                                                throws InterruptedException {
    if (!codetype.equals("empty")) { 
      input_to_editor(inputs.get(howMany + " " + user + " " + codetype + " task code"));
    }
    
    clicks_the_button("submit");
    checkAlert();
  }

  private void input_to_editor(String input) throws InterruptedException {
    driver.findElement(By.id("submission-editor")).click();
    driver.findElement(By.id("submission-editor")).sendKeys(Keys.CONTROL, "a");
    driver.findElement(By.id("submission-editor")).sendKeys(input);
  }
  
  public void checkAlert() {
    try {
      Thread.sleep(LOAD_WAIT_TIME * 2);
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
  
  private void beginNewSession() {
    driver.close();
    driver = new FirefoxDriver();
    driver.manage().deleteAllCookies();
  }
  
  private void navigateToBaseUrl() {  
    driver.get(baseUrl);
  }

  private MultiValueMap<String, String> initializeIds() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    
    map.add("Calculator challenge", "open-challenge-Calculator");
    map.add("challenge name", "challenge_name");
    map.add("challenge description", "challenge_desc");
    map.add("continue live challenge", "continue-live-challenge");
    map.add("create live challenge", "create-live-challenge");
    map.add("Dashboard", "progresscircle");
    map.add("editor page", "Level:");
    map.add("feedback page", "results");
    map.add("feedback page", "next-task-button");
    map.add("front page", "");
    map.add("implement next task page", "Challenge");
    map.add("implement next task page", "next-task-page");
    map.add("implementation task codestub", "impl-task-code-stub");
    map.add("implementation task name", "impl-task-name-input");
    map.add("implementation task description", "impl-task-desc-input");
    map.add("join live challenge", "join-live-challenge");
    map.add("Login", "login-button");
    map.add("login error page", "login?error");
    map.add("Log in", "log-in-button");
    map.add("Logout", "logout-button");
    map.add("My Account", "account-button");
    map.add("new challenge page", "challenge_name");
    map.add("new challenge page", "New challenge");
    map.add("password field", "session_password");
    map.add("task page", "task");
    map.add("Test task multiply", "test-task-1");
    map.add("test task name", "test-task-name-input");
    map.add("test task description", "test-task-desc-input");
    map.add("test task codestub", "test-task-code-stub");
    map.add("new task pair", "test-task-desc-input");
    map.add("new task pair", "test-task-desc-label");
    map.add("new task pair", "New task pair");
    map.add("new task pair", "impl-task-name-input");
    map.add("next task", "next-task-button");
    map.add("submission code", "submission-code");
    map.add("submit", "submit-button");
    map.add("user page", "user");
    map.add("user page", "Dashboard");
    map.add("username field", "session_login");

    map.add("implementation code input by User", inputs.get("first implementation task codestub")
                                                                             .substring(0, 15));
    map.add("test code input by User", inputs.get("first test task codestub").substring(0, 15));
    return map;
  }

  private Map<String, String> initializeTestUrls() {
    Map<String, String> urls = new HashMap<>();
    
    urls.put("user page", "/user");
    urls.put("feedback page", "/feedback");
    urls.put("implement next task page", "/task");
    urls.put("error page", "/error");
    urls.put("new challenge page", "/newchallenge");
    urls.put("new task pair", "/newtaskpair");
    urls.put("front page", "");
    
    return urls;
  }

  private Map<String, String> initializeInputs() {
    Map<String, String> initInputs = new HashMap<>();
    initInputs.put("challenge 1 name", "Calculator");
    initInputs.put("challenge 1 description", "In this challenge you will create "
                                                      + "a perfect Calculator.");
    initInputs.put("first test task name", "test addition");
    initInputs.put("first test task description", "test method addition(int x, int y)");
    initInputs.put("first test task codestub", "public class CalculatorTest "
                                                      + "{\n\tpublic CalculatorTest {}\n}");
    initInputs.put("first implementation task name", "Implement addition");
    initInputs.put("first implementation task description", "Implement method for addition");
    initInputs.put("first implementation task codestub", "public class Calculator"
                                                      + "{\n\tpublic Calculator {}\n}");
    initInputs.put("first Admin implementation task code", "public class Calculator {\n\n"
                                            +  "public void add(int x, int y) {\n"
                                            +  "return x+y;\n"
                                            +  ""); 
    initInputs.put("first User test task code", 
                                               "import org.junit.Test;\n"
                                            +  "import static org.junit.Assert.*;\n"
                                            +  "import fi.helsinki.cs.tmc.edutestutils.Points;\n\n"
                                            +  "@Points(\"03-03\")\n"
                                            +  "public class CalculatorTest {\n\n"
                                            +  "@Test\n"
                                            +  "public void testAddition() {\n"
                                            +  "Calculator calc = new Calculator();\n"
                                            +  "assertEquals(5, calc.add(3,2));\n");
    initInputs.put("first User implementation task code", "public class Calculator {\n\n"
                                            +  "public void add(int x, int y) {\n"
                                            +  "return x+y;\n"
                                            +  ""); 
    initInputs.put("first Admin test task code", 
                                               "import org.junit.Test;\n"
                                            +  "import static org.junit.Assert.*;\n"
                                            +  "import fi.helsinki.cs.tmc.edutestutils.Points;\n\n"
                                            +  "@Points(\"03-03\")\n"
                                            +  "public class CalculatorTest {\n\n"
                                            +  "@Test\n"
                                            +  "public void testAddition() {\n"
                                            +  "Calculator calc = new Calculator();\n"
                                            +  "assertEquals(5, calc.add(3,2));\n");
    
    initInputs.put("second test task name", "test multiplication");
    initInputs.put("second test task description", "test method multiply(int x, int y)");
    initInputs.put("second test task codestub", "public class CalculatorTest "
                                                      + "{\n\tpublic CalculatorTest {}\n}");
    initInputs.put("second implementation task name", "Implement product");
    initInputs.put("second implementation task description", "Implement method for multiplication");
    initInputs.put("second implementation task codestub", "public class Calculator"
                                                      + "{\n\tpublic Calculator {}\n}");
    initInputs.put("second User implementation task code", "public class Calculator {\n\n"
                                            +  "public void multiply(int x, int y) {\n"
                                            +  "return x*y;\n"
                                            +  "\n"
                                            +  "\n"); 
    initInputs.put("second Admin test task code", 
                                               "import org.junit.Test;\n"
                                            +  "import static org.junit.Assert.*;\n"
                                            +  "import fi.helsinki.cs.tmc.edutestutils.Points;\n\n"
                                            +  "@Points(\"03-03\")\n"
                                            +  "public class CalculatorTest {\n\n"
                                            +  "@Test\n"
                                            +  "public void testMultiplication() {\n"
                                            +  "Calculator calc = new Calculator();\n"
                                            +  "assertEquals(6, calc.multiply(3,2));\n");
    initInputs.put("second Admin implementation task code", "public class Calculator {\n\n"
                                            +  "public void multiply(int x, int y) {\n"
                                            +  "return x*y;\n"
                                            +  "\n"
                                            +  "\n"); 
    initInputs.put("second User test task code", 
                                               "import org.junit.Test;\n"
                                            +  "import static org.junit.Assert.*;\n"
                                            +  "import fi.helsinki.cs.tmc.edutestutils.Points;\n\n"
                                            +  "@Points(\"03-03\")\n"
                                            +  "public class CalculatorTest {\n\n"
                                            +  "@Test\n"
                                            +  "public void testMultiplication() {\n"
                                            +  "Calculator calc = new Calculator();\n"
                                            +  "assertEquals(6, calc.multiply(3,2));\n");
    
    return initInputs;
  }

}