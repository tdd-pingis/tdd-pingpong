package pingis.cucumber.pages;

import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlContains;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Heliozoa
 */
public abstract class PageObject {

  protected final RemoteWebDriver driver;
  protected final String title;
  protected final String url;
  protected final String urlRoot;
  protected final String urlTail;

  protected PageObject(RemoteWebDriver driver, String title, String urlTail) {
    this.driver = driver;
    this.title = title;
    this.urlRoot = "http://localhost:8080";
    this.urlTail = urlTail;
    this.url = urlRoot + urlTail;

    String message = String.format(
        "Expected to be on page with"
        + "title containing %s and"
        + "url containing %s but"
        + "title was %s and"
        + "url was %s",
            title, url, driver.getTitle(), driver.getCurrentUrl());
    assertTrue(message, isCurrentPage());
  }

  public final boolean isCurrentPage() {
    try {
      waiting().until(titleContains(title));
      waiting().until(urlContains(urlTail));
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  protected final WebElement findById(String id) {
    return find(By.id(id));
  }

  protected final WebElement findByClassName(String className) {
    return find(By.cssSelector("div[class='" + className + "']"));
  }

  protected final boolean existsById(String id) {
    try {
      findById(id);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  protected boolean contains(WebElement element, String content) {
    try {
      waiting().until(textToBePresentInElement(element, content));
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  protected final boolean containsById(String id, String content) {
    WebElement element = findById(id);
    return contains(element, content);
  }

  protected final boolean containsByClassName(String className, String content) {
    WebElement element = findByClassName(className);
    return contains(element, content);
  }

  protected final void acceptAlert() {
    Alert alert = waiting().until(ExpectedConditions.alertIsPresent());
    alert.accept();
  }

  protected void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(PageObject.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private WebDriverWait waiting() {
    return new WebDriverWait(driver, 30);
  }

  private WebElement find(By by) {
    return waiting().until(presenceOfElementLocated(by));
  }
}
