package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class NewTaskPairPage extends NavbarPage {

  public NewTaskPairPage(RemoteWebDriver driver) {
    super(driver, "#CodePong: New task pair - ", "/newtaskpair/");
  }

  public TaskPage submitWithDefaults() {
    return submitWithDefaults("Default");
  }

  public TaskPage submitWithDefaults(String className) {
    return submitWithDefaults("Addition task", className);
  }

  public TaskPage submitWithDefaults(String taskName, String className) {
    writeTaskName(taskName);
    writeClassName(className);
    writeDescription("This is a " + taskName + " task for the " + className + " class.");
    return submitSuccessfully();
  }

  public void writeTaskName(String taskName) {
    findNameField().sendKeys(taskName);
  }

  public void writeClassName(String className) {
    findClassNameField().sendKeys(className);
  }

  public void writeDescription(String description) {
    findDescriptionField().sendKeys(description);
  }

  public TaskPage submitSuccessfully() {
    submitButton().click();
    return new TaskPage(driver);
  }

  private WebElement findNameField() {
    return findById("task-name-input");
  }

  private WebElement findClassNameField() {
    return findById("class-name-input");
  }

  private WebElement findDescriptionField() {
    return findById("task-desc-input");
  }

  private WebElement submitButton() {
    return findById("submit-button");
  }
}
