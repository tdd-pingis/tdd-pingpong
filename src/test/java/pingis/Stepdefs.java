package pingis;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class Stepdefs {

    @Test
    public void setupTest() {
    }

    @Given("^the test is run$")
    public void the_test_is_run() throws Throwable {
        assertTrue(true);
    }

    @When("^the test has finished$")
    public void the_test_has_finished() throws Throwable {
        assertTrue(true);
    }

    @Then("^there are no errors$")
    public void there_are_no_errors() throws Throwable {
        assertTrue(true);
    }
}