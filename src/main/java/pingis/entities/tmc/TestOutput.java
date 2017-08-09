package pingis.entities.tmc;

import java.util.List;

public class TestOutput {

    //Identical to the Status used by TMC Sandbox results
    public enum Status {
        PASSED,
        TESTS_FAILED,
        COMPILE_FAILED,
        TESTRUN_INTERRUPTED,
        GENERIC_ERROR
    }

    public class Logs {

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
    private List<TestResult> testResults;
    private Logs logs;

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
