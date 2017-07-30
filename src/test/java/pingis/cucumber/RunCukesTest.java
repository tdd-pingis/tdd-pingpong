package pingis.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pingis.Application;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"html:build/reports/tests/cucumber"}, tags = {"~@OAuth"})
public class RunCukesTest {
    @ClassRule
    public static ExternalResource server = new ExternalResource(){
        ConfigurableApplicationContext app;
        
        @Override
        protected void before() throws Throwable {
            this.app = SpringApplication.run(Application.class);
        }
        @Override
        protected void after() {
            app.close();
        }
    };
}