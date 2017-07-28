package pingis;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class Stepdefs {
    
    WebDriver driver;
    String baseUrl;
    String username = System.getenv("TMC_TEST_USER_LOGIN");
    String password = System.getenv("TMC_TEST_USER_PASSWORD");

    @Test
    public void setupTest() {
    }

    @Before
    public void setUp() throws Exception {
        if(driver == null) driver = new HtmlUnitDriver();
        driver.manage().deleteAllCookies();
        baseUrl = "http://localhost:8080/";
    }

    private void get(String page){
        driver.get(baseUrl + page);
    }

    private boolean contains(String s){
        return driver.getPageSource().contains(s);
    }

    @Given("^.* navigates to the login form$")
    public void navigates_to_the_login_form() throws Throwable {
        get("/login");
    }

    @Given("^.* navigates to the task page at (.*)$")
    public void navigates_to_the_task_page(String address) throws Throwable {
        get(address);
    }

    @When("^.* clicks the (.*)$")
    public void clicks_the_element(String element) {
        driver.findElement(By.linkText(element)).click();
    }

    @And("^chooses to authenticate with TMC$")
    public void chooses_to_authenticate_with_tmc() throws Throwable {
        driver.findElement(By.linkText("TMC")).click();
    }

    @When("^.* inputs their credentials for TMC$")
    public void inputs_credentials_for_tmc() throws Throwable {
        driver.findElement(By.id("session_login")).sendKeys(username);
        driver.findElement(By.id("session_password")).sendKeys(password);
    }

    @And("^submits the TMC login form$")
    public void submits_tmc() throws Throwable {
        driver.findElement(By.name("commit")).click();
    }

    @And("^gives their authorization$")
    public void gives_their_authorization() throws Throwable {
        driver.findElement(By.name("commit")).click();
    }

    @Then("^.* is successfully authenticated$")
    public void successfully_authenticated() throws Throwable {
        assertTrue(contains("Authorization code"));
    }

    @Then("^the page contains (.*)$")
    public void page_has_the_right_content(String content) {
        assertTrue(contains(content));
    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
