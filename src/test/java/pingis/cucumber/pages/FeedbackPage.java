package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class FeedbackPage extends NavbarPage {

  public FeedbackPage(RemoteWebDriver driver) {
    super(driver, "#CodePong: Feedback", "/feedback");
  }

  public boolean successMessageIsShown() {
    return contains(findResultsPanel(), "Task cleared!");
  }

  public NewTaskPairPage continueToNextTask() {
    findNextTaskButton().click();
    return new NewTaskPairPage(driver);
  }

  private WebElement findResultsPanel() {
    return findById("results-panel");
  }

  private WebElement findNextTaskButton() {
    return findById("next-task-button");
  }
}
