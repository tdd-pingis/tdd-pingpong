package pingis.entities.tmc;

import java.util.List;

public class TestResult {
    
    private String name;
    private boolean passed;
    private List<String> points;
    private String errorMessage;
    private List<String> backtrace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getBacktrace() {
        return backtrace;
    }

    public void setBacktrace(List<String> backtrace) {
        this.backtrace = backtrace;
    }
}
