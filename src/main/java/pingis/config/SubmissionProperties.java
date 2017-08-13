package pingis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dwarfcrank on 7/28/17.
 */
@Configuration
@ConfigurationProperties(prefix = "tmc")
public class SubmissionProperties {

  private String sandboxUrl;
  private String notifyUrl;

  public String getNotifyUrl() {
    return notifyUrl;
  }

  public void setNotifyUrl(String notifyUrl) {
    this.notifyUrl = notifyUrl;
  }

  public String getSandboxUrl() {
    return sandboxUrl;
  }

  public void setSandboxUrl(String sandboxUrl) {
    this.sandboxUrl = sandboxUrl;
  }
}
