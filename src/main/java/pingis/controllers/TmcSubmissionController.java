package pingis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pingis.entities.TmcSubmission;
import pingis.entities.TmcSubmissionStatus;
import pingis.repositories.TmcSubmissionRepository;

import java.util.Optional;
import java.util.UUID;

@Controller
public class TmcSubmissionController {
    @Autowired
    private TmcSubmissionRepository submissionRepository;

    // These request parameters are specified separately because there doesn't seem to
    // be a simple way to rename fields when doing data binding.
    @PostMapping("/submission-result")
    public ResponseEntity submissionResult(
            @RequestParam("test_output") String testOutput,
            @RequestParam String stdout,
            @RequestParam String stderr,
            @RequestParam String validations,
            @RequestParam("vm_log") String vmLog,
            @RequestParam String token,
            @RequestParam String status,
            @RequestParam("exit_code") String exitCode) {
        UUID submissionId = UUID.fromString(token);
        TmcSubmission submission = submissionRepository.findOne(submissionId);

        if (submission == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (submission.getStatus() != TmcSubmissionStatus.PENDING) {
            // Result is being submitted twice.
            // TODO: decide on a better response code for this...
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int exitCodeValue = Integer.parseInt(exitCode.trim());

        submission.setExitCode(exitCodeValue);
        submission.setStatus(status);
        submission.setStderr(stderr);
        submission.setStdout(stdout);
        submission.setTestOutput(testOutput);
        submission.setValidations(validations);
        submission.setVmLog(vmLog);

        submissionRepository.save(submission);

        return new ResponseEntity(HttpStatus.OK);
    }
}
