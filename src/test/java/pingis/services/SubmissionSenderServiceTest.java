package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import pingis.config.SandboxSubmissionProperties;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.repositories.sandbox.SubmissionRepository;
import pingis.services.sandbox.SubmissionResponse;
import pingis.services.sandbox.SubmissionSenderService;

@RunWith(SpringRunner.class)
@RestClientTest(SubmissionSenderService.class)
@TestPropertySource(properties = {"tmc.sandboxUrl=http://localhost:3001",
    "tmc.notifyUrl=http://localhost:1337"})
@ContextConfiguration(classes = {SandboxSubmissionProperties.class, SubmissionSenderService.class})
@DirtiesContext
public class SubmissionSenderServiceTest {

  @Autowired
  private SubmissionSenderService sender;

  @Autowired
  private MockRestServiceServer server;

  @Test
  public void testSubmit()
      throws ExecutionException, InterruptedException {
    String packageData = "this_is_a_package";

    // This test is *very* limited as Spring doesn't have any easy way to check
    // requests with multipart form data...
    server.expect(requestTo("/tasks.json"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().contentTypeCompatibleWith(MediaType.MULTIPART_FORM_DATA))
        .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

    UUID submissionId = UUID.randomUUID();

    CompletableFuture<SubmissionResponse> responseFuture = sender.sendSubmission(submissionId, packageData.getBytes());
    SubmissionResponse response = responseFuture.get();

    assertTrue(response.getStatus().equals(SubmissionResponse.OK));
  }
}