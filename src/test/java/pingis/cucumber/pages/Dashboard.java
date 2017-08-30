package pingis.cucumber.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Heliozoa
 */
public class Dashboard extends NavbarPage {

  public Dashboard(RemoteWebDriver driver) {
    super(driver, "#CodePong: Dashboard - ", "/user");
  }

  public NewTaskPairPage startAnyArcade() {
    findAnyStartArcadeButton().click();
    return new NewTaskPairPage(driver);
  }

  public NewChallengePage createNewLiveChallenge() {
    findStartChallengeButton().click();
    return new NewChallengePage(driver);
  }

  public TaskPage joinLiveChallenge() {
    // If this breaks, add sleep
    findJoinChallengeButton().click();
    return new TaskPage(driver);
  }

  public TaskPage continueLiveChallenge() {
    // If this breaks, add sleep
    findContinueChallengeButton().click();
    return new TaskPage(driver);
  }

  public Dashboard continueLiveChallengeUnsuccessfully() {
    findContinueChallengeButton().click();
    return new Dashboard(driver);
  }

  private WebElement findStartChallengeButton() {
    return findById("create-challenge");
  }

  private WebElement findJoinChallengeButton() {
    return findById("join-challenge");
  }

  private WebElement findContinueChallengeButton() {
    return findById("continue-challenge");
  }

  private WebElement findAnyStartArcadeButton() {
    // TEMPORARY
    // AFAIK some of the arcade buttons don't work at the moment,
    // so a specific one is being selected here
    return findById("oop-arcade-button");
  }
}
