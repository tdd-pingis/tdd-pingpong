package pingis;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})
public class RunCukesTest {
    public static final int PORT = 8080;
    @ClassRule
    public static ServerRule server = new ServerRule(PORT);
}