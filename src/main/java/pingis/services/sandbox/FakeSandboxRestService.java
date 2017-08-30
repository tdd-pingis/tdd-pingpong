package pingis.services.sandbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pingis.entities.TaskType;
import pingis.entities.sandbox.ResultStatus;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.entities.sandbox.TestOutput;
import pingis.repositories.sandbox.SubmissionRepository;

/**
 *
 * @author juicyp
 */
@Service
@Profile("dev")
public class FakeSandboxRestService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private RestTemplate restTemplate;

  private SubmissionRepository submissionRepository;

  @Autowired
  public FakeSandboxRestService(RestTemplateBuilder restTemplateBuilder,
      SubmissionRepository submissionRepository) {
    this.submissionRepository = submissionRepository;
    this.restTemplate = restTemplateBuilder.build();
  }

  public void postSubmissionResults(String token, String notify) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> request = buildResponseEntity(
        generateSubmission(token), headers);

    logger.debug("Posting fake results");
    restTemplate.postForLocation(notify, request, String.class);
  }

  private Submission generateSubmission(String token) {
    logger.debug("Generating submission");
    Submission submission = submissionRepository.findById(UUID.fromString(token)).get();

    submission.setId(UUID.fromString(token));
    submission.setStatus(SubmissionStatus.PENDING);
    submission.setExitCode(0);
    submission.setStderr("");
    submission.setStdout("");

    TaskType type = submission.getTaskInstance().getTask().getType();
    ResultStatus resultStatus
        = (type == TaskType.IMPLEMENTATION) ? ResultStatus.PASSED : ResultStatus.TESTS_FAILED;

    try {
      submission.setTestOutput(new ObjectMapper().readValue(
          "{\"status\":\"" + resultStatus + "\","
          + "\"testResults\":[],"
          + "\"logs\":{"
          + "\"stdout\":[0],"
          + "\"stderr\":[0]"
          + "}"
          + "}", TestOutput.class));
    } catch (IOException ex) {
      logger.debug("JSON serialization failed");
    }
    submission.setValidations("");
    submission.setVmLog("");

    return submission;
  }

  private HttpEntity<MultiValueMap<String, String>> buildResponseEntity(
      Submission submission, HttpHeaders headers) {
    logger.debug("Building response entity");
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    try {
      map.add("test_output", new ObjectMapper().writeValueAsString(
          submission.getTestOutput()));
    } catch (JsonProcessingException ex) {
      logger.debug("POJO deserialization failed");
    }
    map.add("stdout", submission.getStdout());
    map.add("stderr", submission.getStderr());
    map.add("validations", submission.getValidations());
    map.add("vm_log", submission.getVmLog());
    map.add("token", submission.getId().toString());
    map.add("status", submission.getStatus().toString());
    map.add("exit_code", submission.getExitCode().toString());

    return new HttpEntity<>(map, headers);
  }
}
