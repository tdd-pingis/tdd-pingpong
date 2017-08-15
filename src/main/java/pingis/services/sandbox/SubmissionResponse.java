package pingis.services.sandbox;

public class SubmissionResponse {

  public static final String OK = "ok";
  public static final String BAD_REQUEST = "bad_request";

  private String status;

  public SubmissionResponse() {
  }

  public String getStatus() {
    return status;
  }

  private void checkStatus(String status) {
    if (!status.equals(OK) && !status.equals(BAD_REQUEST)) {
      throw new IllegalArgumentException("Invalid status.");
    }
  }

  public void setStatus(String status) {
    checkStatus(status);

    this.status = status;
  }
}
