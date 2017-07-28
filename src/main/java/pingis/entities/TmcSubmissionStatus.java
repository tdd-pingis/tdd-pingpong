package pingis.entities;

public enum TmcSubmissionStatus {
    // Initial state, when the submission has been created.
    // Not actually used by the sandbox.
    PENDING,

    FINISHED,
    TIMEOUT,
    FAILED
}
