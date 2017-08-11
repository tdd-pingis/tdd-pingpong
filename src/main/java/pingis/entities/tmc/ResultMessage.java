/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities.tmc;

import java.util.List;
import pingis.entities.TaskType;

/**
 *
 * @author authority
 */
public class ResultMessage {
    private boolean success;
    private TaskType type;
    private ResultStatus status;
    
    private String stdout;
    private List<TestResult> passedTests;
    private List<TestResult> failedTests;

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

    public List<TestResult> getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(List<TestResult> passedTests) {
        this.passedTests = passedTests;
    }

    public List<TestResult> getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(List<TestResult> failedTests) {
        this.failedTests = failedTests;
    }
}
