package pingis.services.sandbox;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pingis.config.SandboxSubmissionProperties;
import pingis.entities.sandbox.Submission;
import pingis.repositories.sandbox.SubmissionRepository;

@Service
public class SubmissionSenderService {

  private final Logger logger = LoggerFactory.getLogger(SubmissionSenderService.class);

  private static final String SUBMISSION_FILENAME = "submission.tar";

  private RestTemplate restTemplate;

  private final SandboxSubmissionProperties sandboxSubmissionProperties;

  @Autowired
  public SubmissionSenderService(RestTemplateBuilder restTemplateBuilder,
      SandboxSubmissionProperties sandboxSubmissionProperties) {
    this.sandboxSubmissionProperties = sandboxSubmissionProperties;

    restTemplate = restTemplateBuilder
        .rootUri(sandboxSubmissionProperties.getSandboxUrl())
        .build();
  }

  private HttpEntity<MultiValueMap<String, Object>> buildRequestEntity(byte[] packaged,
      String notifyToken,
      String notifyUrl) {
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

    HttpHeaders textHeaders = new HttpHeaders();
    textHeaders.setContentType(MediaType.TEXT_PLAIN);

    HttpHeaders binaryHeaders = new HttpHeaders();
    binaryHeaders.setContentDispositionFormData("file", SUBMISSION_FILENAME);

    map.add("file", new HttpEntity<>(new ByteArrayResource(packaged), binaryHeaders));
    map.add("token", new HttpEntity<>(notifyToken, textHeaders));
    map.add("notify", new HttpEntity<>(notifyUrl, textHeaders));

    return new HttpEntity<>(map);
  }

  @Async
  public CompletableFuture<SubmissionResponse> sendSubmission(Submission submission, byte[] packaged) {
    logger.debug("Created new submission, id: {}", submission.getId());

    String notifyUrl = sandboxSubmissionProperties.getNotifyUrl();

    HttpEntity<MultiValueMap<String, Object>> requestEntity = buildRequestEntity(packaged,
        submission.getId().toString(), notifyUrl);

    SubmissionResponse response = restTemplate.postForObject("/tasks.json", requestEntity,
        SubmissionResponse.class);

    logger.debug("Received sandbox response: {}", response.getStatus());

    return CompletableFuture.completedFuture(response);
  }
}
