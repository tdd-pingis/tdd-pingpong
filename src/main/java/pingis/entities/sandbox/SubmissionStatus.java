package pingis.entities.sandbox;

public enum SubmissionStatus {
  // Initial state, when the submission has been created.
  // Not actually used by the sandbox.
  PENDING,
  FINISHED,
  TIMEOUT,
  FAILED
}
