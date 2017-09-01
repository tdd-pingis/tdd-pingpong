package pingis.services.sandbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import pingis.entities.sandbox.RequestedStatus;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
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

  private final String testsPassResponsePath = "resultMessages/testsPass";
  private final String testsFailResponsePath = "resultMessages/testsFail";
  private final String compileFailedResponsePath = "resultMessages/compileFailed";

  @Autowired
  public FakeSandboxRestService(RestTemplateBuilder restTemplateBuilder,
      SubmissionRepository submissionRepository) {

    this.submissionRepository = submissionRepository;
    this.restTemplate = restTemplateBuilder.build();
  }

  public void postSubmissionResults(String token, String notify, RequestedStatus requestedStatus) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    Submission submission = generateSubmission(token, requestedStatus);

    HttpEntity<MultiValueMap<String, String>> request = buildResponseEntity(
        submission, headers);

    logger.debug("Posting fake results");
    restTemplate.postForLocation(notify, request, String.class);
  }

  private Submission generateSubmission(String token, RequestedStatus requestedStatus) {
    logger.debug("Generating submission");

    String path = choosePathByRequestedStatus(requestedStatus, token);

    Submission submission = submissionResultsFromFile(path, token);

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
      logger.debug("POJO deserialization failed: {}", ex.getMessage());
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

  private String choosePathByRequestedStatus(
          RequestedStatus requestedStatus, String token) {

    switch (requestedStatus) {
      case TESTS_PASSED:
        return testsPassResponsePath;
      case TESTS_FAILED:
        return testsFailResponsePath;
      case COMPILE_FAILED:
        return compileFailedResponsePath;
      case TASK_PASSING:
      default:
        return choosePathForPassingTask(token);
    }
  }

  private String choosePathForPassingTask(String token) {
    Submission submission = submissionRepository.findById(UUID.fromString(token)).get();
    TaskType type = submission.getTaskInstance().getTask().getType();

    return (type == TaskType.IMPLEMENTATION)
            ? testsPassResponsePath : testsFailResponsePath;
  }

  private Submission submissionResultsFromFile(String path, String token) {
    Submission submission = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      URI uri = ClassLoader.getSystemResource(path).toURI();
      submission = mapper.readValue(new File(uri), Submission.class);
      submission.setId(UUID.fromString(token));
      submission.setStatus(SubmissionStatus.PENDING);
    } catch (URISyntaxException | IOException | NullPointerException ex) {
      logger.debug("Failed to load submission from file: {}", ex.getMessage());
    }
    return submission;
  }
}
