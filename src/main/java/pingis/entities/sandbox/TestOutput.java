package pingis.entities.sandbox;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class TestOutput {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  Submission tmcSubmission;

  private ResultStatus status;

  @OneToMany(cascade = {CascadeType.ALL})
  private List<TestResult> testResults;

  @OneToOne(cascade = {CascadeType.ALL})
  private Logs logs;

  public long getId() {
    return id;
  }

  public ResultStatus getStatus() {
    return status;
  }

  public void setStatus(ResultStatus status) {
    this.status = status;
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public void setTestResults(List<TestResult> testResults) {
    this.testResults = testResults;
  }

  public Logs getLogs() {
    return logs;
  }

  public void setLogs(Logs logs) {
    this.logs = logs;
  }
}
