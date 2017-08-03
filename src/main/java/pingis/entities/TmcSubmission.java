package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created by dwarfcrank on 7/28/17.
 */
@Entity
public class TmcSubmission {
    @Id
    private UUID id;

    private Integer exitCode;
    private String stderr;
    private String stdout;
    private TmcSubmissionStatus status;
    private String testOutput;
    private String validations;
    private String vmLog;

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

    public String getTestOutput() {
        return testOutput;
    }

    public void setTestOutput(String testOutput) {
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
}
