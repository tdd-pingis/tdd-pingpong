package pingis.cucumber;

import static org.junit.Assert.assertTrue;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.cucumber.pages.Dashboard;
import pingis.cucumber.pages.FeedbackPage;
import pingis.cucumber.pages.Frontpage;
import pingis.cucumber.pages.LoginPage;
import pingis.cucumber.pages.NavbarPage;
import pingis.cucumber.pages.NewChallengePage;
import pingis.cucumber.pages.NewTaskPairPage;
import pingis.cucumber.pages.TaskPage;
import pingis.services.DataImporter;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration
public class Stepdefs {

  private static final String baseUrl = "http://localhost:8080/";

  RemoteWebDriver driver;

  @Autowired
  DataImporter dataImporter;

  //Page objects
  Dashboard dashboard;
  FeedbackPage feedbackPage;
  Frontpage frontpage;
  LoginPage loginPage;
  NavbarPage navbar;
  NewChallengePage newChallengePage;
  NewTaskPairPage newTaskPairPage;
  TaskPage taskPage;

  @Test
  public void setupTest() {}

  @Before
  public void setUp() throws Exception {
    if (driver == null) {
      driver = new FirefoxDriver();
      driver.manage().deleteAllCookies();
    }

    dataImporter.dropDatabase();
    dataImporter.initializeDatabase();

    navigateToBaseUrl();
  }

  private void navigateToBaseUrl() {
    driver.get(baseUrl);
  }

  @When(".*(?:logs|is logged) in with username (.+) and password (.+)")
  public void user_logs_in_with(String username, String password) {

    navbar = new NavbarPage(driver);
    loginPage = navbar.navigateToLoginPage();
    dashboard = loginPage.loginSuccessfully(username, password);
  }

  @When(".*logs in with incorrect username (.+) and password (.+)")
  public void user_logs_in_with_incorrect(String username, String password) {

    navbar = new NavbarPage(driver);
    loginPage = navbar.navigateToLoginPage();
    loginPage = loginPage.loginUnsuccessfully(username, password);
  }

  @Then(".*is successfully authenticated$")
  public void is_successfully_authenticated() {
    navbar = new NavbarPage(driver);
    assertTrue(navbar.currentUserIsAuthenticated());
  }

  @Then(".*is (?:not|no longer) authenticated$")
  public void is_not_successfully_authenticated() {
    navbar = new NavbarPage(driver);
    assertTrue(navbar.currentUserIsNotAuthenticated());
  }

  @And(".*is redirected to the user page$")
  public void is_redirected_to_user_page() {
    // A PageObject's constructor asserts
    // that it is instantiated only when
    // it corresponds to the actual current page
    new Dashboard(driver);
  }

  @And(".*is redirected to the login page$")
  public void is_redirected_to_login_page() {
    // A PageObject's constructor asserts
    // that it is instantiated only when
    // it corresponds to the actual current page
    new LoginPage(driver);
  }

  @And(".*is redirected to the dashboard")
  public void is_redirected_to_dashboard() {
    // A PageObject's constructor asserts
    // that it is instantiated only when
    // it corresponds to the actual current page
    new Dashboard(driver);
  }

  @Then(".*is redirected to the new task pair page$")
  public void is_redirected_to_new_task_pair() {
    // A PageObject's constructor asserts
    // that it is instantiated only when
    // it corresponds to the actual current page
    new NewTaskPairPage(driver);
  }

  @When(".*(?:logs|logged) out$")
  public void logs_out() {

    navbar = new NavbarPage(driver);
    frontpage = navbar.logout();
  }

  @Given(".*has a task open for class (.+)")
  public void has_task_open_for_class(String className) {
    navigateToBaseUrl();

    dashboard = new Dashboard(driver);
    newTaskPairPage = dashboard.startAnyArcade();
    taskPage = newTaskPairPage.submitWithDefaults(className);
  }

  @When(".*clicks the first tab$")
  public void clicks_the_first_tab() {
    taskPage = new TaskPage(driver);
    taskPage.openFirstTab();
  }

  @When(".*clicks the second tab$")
  public void clicks_the_second_tab() {
    taskPage = new TaskPage(driver);
    taskPage.openSecondTab();
  }

  @Then(".*first editor contains (.+)")
  public void first_editor_contains(String content) {
    taskPage = new TaskPage(driver);
    taskPage.openFirstTab();
    assertTrue(taskPage.activeEditorContains(content));
  }

  @Then(".*second editor contains (.+)")
  public void second_editor_contains(String content) {
    taskPage = new TaskPage(driver);
    taskPage.openSecondTab();
    assertTrue(taskPage.activeEditorContains(content));
  }

  @When("(.+) submits correct code$")
  public void user_inputs_correct_code(String user) {
    taskPage = new TaskPage(driver);
    feedbackPage = taskPage.submitCorrectCode();
  }

  @Then(".*is redirected to the feedback page$")
  public void is_redirected_to_feedback_page() {
    // A PageObject's constructor asserts
    // that it is instantiated only when
    // it corresponds to the actual current page
    new FeedbackPage(driver);
  }

  @And("^feedback of success is shown$")
  public void feedback_success_is_shown() {
    feedbackPage = new FeedbackPage(driver);
    assertTrue(feedbackPage.successMessageIsShown());
  }

  @When(".*(?:creates|created) a new Live challenge (.+) and an? (.+) task pair")
  public void creates_new_live_challenge(String challengeName, String taskName) {
    dashboard = new Dashboard(driver);
    newChallengePage = dashboard.createNewLiveChallenge();
    newTaskPairPage = newChallengePage
            .submitWithDefaults(challengeName);
    newTaskPairPage.submitWithDefaults(taskName, challengeName);
  }

  @When(".*(?:creates|created) a new Live challenge and a task pair")
  public void creates_new_live_challenge() {
    dashboard = new Dashboard(driver);
    newChallengePage = dashboard.createNewLiveChallenge();
    newTaskPairPage = newChallengePage
            .submitWithDefaults();
    newTaskPairPage.submitWithDefaults();
  }

  @When(".*creates a new (.+) task pair for (.+)$")
  public void creates_new_task_pair(String taskName, String className) {
    newTaskPairPage = new NewTaskPairPage(driver);
    newTaskPairPage.submitWithDefaults(taskName, className);
  }

  @Then("^the (.+) .+ task for (.+) is shown$")
  public void new_task_pair_is_shown(String taskName, String challengeName) {
    taskPage = new TaskPage(driver);
    assertTrue(taskPage.challengeNameContains(challengeName));
    assertTrue(taskPage.taskNameContains(taskName));
  }

  @Given(".*(?:has submit|submits) .+ code containing (.+)$")
  public void has_submit_code_containing(String code) {
    taskPage = new TaskPage(driver);
    taskPage.addCode(code);
    feedbackPage = taskPage.submitCorrectCode();
  }

  @Given(".*has submit .+ code$")
  public void has_submit_code() {
    taskPage = new TaskPage(driver);
    feedbackPage = taskPage.submitCorrectCode();
  }

  @When(".*joins the Live challenge")
  public void joins_live_challenge() {
    dashboard = new Dashboard(driver);
    taskPage = dashboard.joinLiveChallenge();
  }

  @When(".*continues the Live challenge")
  public void continues_live_challenge() {
    dashboard = new Dashboard(driver);
    taskPage = dashboard.continueLiveChallenge();
  }

  @When(".*returns to the dashboard")
  public void to_dashboard() {
    navbar = new NavbarPage(driver);
    dashboard = navbar.navigateToDashboard();
  }

  @When(".*tries to continue the challenge$")
  public void continue_challenge() {
    dashboard = new Dashboard(driver);
    dashboard = dashboard.continueLiveChallengeUnsuccessfully();
  }

  @And(".*continues to the next task$")
  public void continues_to_next_task() {
    feedbackPage = new FeedbackPage(driver);
    newTaskPairPage = feedbackPage.continueToNextTask();
  }

  @After
  public void closeDriver() {
    driver.close();
  }
}
