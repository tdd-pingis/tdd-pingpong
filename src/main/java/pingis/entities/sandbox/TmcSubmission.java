package pingis.entities.sandbox;

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
public class TmcSubmission {

  @Id
  private UUID id;

  private Integer exitCode;

  @Lob
  private String stderr;

  @Lob
  private String stdout;

  private TmcSubmissionStatus status;

  @OneToOne(cascade = {CascadeType.ALL})
  private TestOutput testOutput;

  @Lob
  private String validations;

  @Lob
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

  public TmcSubmissionStatus getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = TmcSubmissionStatus.valueOf(status.toUpperCase());
  }

  public void setStatus(TmcSubmissionStatus status) {
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
