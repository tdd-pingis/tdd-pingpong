package pingis.entities.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import pingis.entities.TaskInstance;

/**
 * Created by dwarfcrank on 7/28/17.
 */
@Entity
@JsonIgnoreProperties(value = {"taskInstance"})
public class Submission {

  @Id
  @JsonProperty("token")
  private UUID id;

  @JsonProperty("exit_code")
  private Integer exitCode;

  @Lob
  private String stderr;

  @Lob
  private String stdout;

  private SubmissionStatus status;

  @OneToOne(cascade = {CascadeType.ALL})
  @JsonProperty("test_output")
  private TestOutput testOutput;

  @Lob
  private String validations;

  @Lob
  @JsonProperty("vm_log")
  private String vmLog;

  @OneToOne
  private TaskInstance taskInstance;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Integer getExitCode() {
    return exitCode;
  }

  public void setExitCode(Integer exitCode) {
    this.exitCode = exitCode;
  }

  public String getStderr() {
    return stderr;
  }

  public void setStderr(String stderr) {
    this.stderr = stderr;
  }

  public String getStdout() {
    return stdout;
  }

  public void setStdout(String stdout) {
    this.stdout = stdout;
  }

  public SubmissionStatus getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = SubmissionStatus.valueOf(status.toUpperCase());
  }

  public void setStatus(SubmissionStatus status) {
    this.status = status;
  }

  public TestOutput getTestOutput() {
    return testOutput;
  }

  public void setTestOutput(TestOutput testOutput) {
    this.testOutput = testOutput;
  }

  public String getValidations() {
    return validations;
  }

  public void setValidations(String validations) {
    this.validations = validations;
  }

  public String getVmLog() {
    return vmLog;
  }

  public void setVmLog(String vmLog) {
    this.vmLog = vmLog;
  }

  public void setTaskInstance(TaskInstance taskInstance) {
    this.taskInstance = taskInstance;
  }

  public TaskInstance getTaskInstance() {
    return taskInstance;
  }
}
