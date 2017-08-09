package pingis.entities.tmc;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class TestOutput {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @OneToOne
    TmcSubmission tmcSubmission;

    //Identical to the Status used by TMC Sandbox results
    public enum Status {
        PASSED,
        TESTS_FAILED,
        COMPILE_FAILED,
        TESTRUN_INTERRUPTED,
        GENERIC_ERROR
    }

    @Entity
    public class Logs {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private long id;
        
        @OneToOne
        private TestOutput testOutput;
        
        private byte[] stdout;
        private byte[] stderr;

        public byte[] getStdout() {
            return stdout;
        }
        
        public void setStdout(byte[] stdout) {
            this.stdout = stdout;
        }

        public byte[] getStderr() {
            return stderr;
        }
        
        public void setStderr(byte[] stderr) {
            this.stderr = stderr;
        }
    }

    private Status status;
    
    @OneToMany
    private List<TestResult> testResults;
    
    @OneToOne
    private Logs logs;

    public long getId() {
        return id;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
