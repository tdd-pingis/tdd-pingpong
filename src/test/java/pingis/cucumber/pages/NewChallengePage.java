package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class NewChallengePage extends NavbarPage {

  public NewChallengePage(RemoteWebDriver driver) {
    super(driver, "#CodePong: New challenge", "/newchallenge");
  }

  public NewTaskPairPage submitWithDefaults() {
    return submitWithDefaults("Default");
  }

  public NewTaskPairPage submitWithDefaults(String name) {
    writeName(name);
    writeDescription("This is a " + name + " challenge!");
    return submitCorrectChallenge();
  }

  public void writeName(String name) {
    findNameField().sendKeys(name);
  }

  public void writeDescription(String description) {
    findDescriptionField().sendKeys(description);
  }

  public NewTaskPairPage submitCorrectChallenge() {
    findSubmitButton().click();
    return new NewTaskPairPage(driver);
  }

  private WebElement findNameField() {
    return findById("challenge_name");
  }

  private WebElement findDescriptionField() {
    return findById("challenge_desc");
  }

  private WebElement findSubmitButton() {
    return findById("submit-button");
  }
}
