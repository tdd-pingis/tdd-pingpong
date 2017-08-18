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
@CucumberOptions(plugin = {"html:build/reports/tests/cucumberReport"})
public class RunCukesTest {

  @ClassRule
  public static ExternalResource server = new ExternalResource() {
    ConfigurableApplicationContext app;

    @Override
    protected void before() throws Throwable {
      this.app = SpringApplication.run(Application.class);
      app.getEnvironment().getSystemEnvironment().put("RUNNING_CUCUMBER", "TRUE");
    }

    @Override
    protected void after() {
      app.close();
    }
  };
}