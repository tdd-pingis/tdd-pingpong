package pingis.entities.sandbox;

public enum SubmissionStatus {
  // Initial state, when the submission has been created.
  // Not actually used by the sandbox.
  PENDING,

  // Not actually used by the sandbox. Indicates that sending the submission
  // itself failed.
  SEND_FAILED,

  FINISHED,
  TIMEOUT,
  FAILED
}
