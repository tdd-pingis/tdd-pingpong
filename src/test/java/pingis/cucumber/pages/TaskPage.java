package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class TaskPage extends NavbarPage {

  public TaskPage(RemoteWebDriver driver) {
    super(driver, "#CodePong: Editor", "/task/");
  }

  public void addCode(String code) {
    String existingCode =
            (String) driver.executeScript("return submissionEditor.getValue();");
    driver.executeScript("submissionEditor.setValue(`" + existingCode + " //" + code + "`);");
  }

  public boolean activeEditorContains(String content) {
    return containsByClassName("tab-pane fade active in", content);
  }

  public boolean challengeNameContains(String name) {
    return contains(findChallengeName(), name);
  }

  public boolean taskNameContains(String name) {
    return contains(findTaskName(), name);
  }

  public FeedbackPage submitCorrectCode() {
    submitCode();
    return new FeedbackPage(driver);
  }

  public void openFirstTab() {
    findFirstTab().click();
  }

  public void openSecondTab() {
    findSecondTab().click();
  }

  private void submitCode() {
    findSubmitButton().click();
    acceptAlert();
  }

  private WebElement findFirstTab() {
    return findById("first-tab");
  }

  private WebElement findSecondTab() {
    return findById("second-tab");
  }

  private WebElement findSubmitButton() {
    return findById("submit-button");
  }

  private WebElement findChallengeName() {
    return findById("challenge-name");
  }

  private WebElement findTaskName() {
    return findById("task-name");
  }

  private WebElement findTaskDescription() {
    return findById("task-desc");
  }
}
