package pingis.entities.sandbox;

import java.util.List;
import pingis.entities.TaskType;

/**
 * @author authority
 */
public class ResultMessage {

  private boolean success;
  private TaskType type;
  private ResultStatus status;

  private String stdout;
  private List<TestResult> tests;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public TaskType getType() {
    return type;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  public ResultStatus getStatus() {
    return status;
  }

  public void setStatus(ResultStatus status) {
    this.status = status;
  }

  public String getStdout() {
    return stdout;
  }

  public void setStdout(String stdout) {
    this.stdout = stdout;
  }

  public void setTests(List<TestResult> tests) {
    this.tests = tests;
  }

  public List<TestResult> getTests() {
    return tests;
  }
}
