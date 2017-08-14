package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
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
import pingis.config.SubmissionProperties;
import pingis.entities.tmc.TmcSubmission;
import pingis.entities.tmc.TmcSubmissionStatus;
import pingis.repositories.TmcSubmissionRepository;

@RunWith(SpringRunner.class)
@RestClientTest(SubmissionSenderService.class)
@TestPropertySource(properties = {"tmc.sandboxUrl=http://localhost:3001",
    "tmc.notifyUrl=http://localhost:1337"})
@ContextConfiguration(classes = {SubmissionProperties.class, SubmissionSenderService.class})
@DirtiesContext
public class SubmissionSenderServiceTest {

  @Autowired
  private SubmissionSenderService sender;

  @Autowired
  private MockRestServiceServer server;

  @MockBean
  private TmcSubmissionRepository submissionRepository;

  @Test
  public void testSubmit() throws IOException, ArchiveException {
    String packageData = "this_is_a_package";

    // This test is *very* limited as Spring doesn't have any easy way to check
    // requests with multipart form data...
    server.expect(requestTo("/tasks.json"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().contentTypeCompatibleWith(MediaType.MULTIPART_FORM_DATA))
        .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

    TmcSubmission submission = new TmcSubmission();
    sender.sendSubmission(submission, packageData.getBytes());

    ArgumentCaptor<TmcSubmission> submissionCaptor = ArgumentCaptor.forClass(TmcSubmission.class);
    verify(submissionRepository, times(1)).save(submissionCaptor.capture());
    verifyNoMoreInteractions(submissionRepository);

    TmcSubmission captured = submissionCaptor.getValue();

    assertNotNull(submission);
    assertEquals(captured, submission);
    assertEquals(TmcSubmissionStatus.PENDING, captured.getStatus());
  }
}