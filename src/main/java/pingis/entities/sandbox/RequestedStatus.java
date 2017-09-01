package pingis.entities.sandbox;

import org.springframework.context.annotation.Profile;

/**
 *
 * @author authority
 */
@Profile("dev")
public enum RequestedStatus {
  TESTS_PASSED,
  TESTS_FAILED,
  COMPILE_FAILED,
  TASK_PASSING;

  public byte[] asBytes() {
    return this.toString().getBytes();
  }
}
