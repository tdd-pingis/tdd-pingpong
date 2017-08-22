package pingis.services.sandbox;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import pingis.config.SandboxSubmissionProperties;
import pingis.config.SecurityConfig;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.repositories.sandbox.SubmissionRepository;
import pingis.services.sandbox.SubmissionSenderService;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import pingis.Application;
import pingis.config.OAuthProperties;

/**
 *
 * @author juicyp
 */
@RunWith(SpringRunner.class)
@RestClientTest(FakeSandboxRestService.class)
@ContextConfiguration(classes = {FakeSandboxRestService.class})
@DirtiesContext
public class FakeSandboxRestServiceTest {
  
  @Autowired
  private FakeSandboxRestService fakeSandboxRestService;
  
  @MockBean
  private SubmissionRepository submissionRepositoryMock;
  
  @Autowired
  private MockRestServiceServer server;
  
  private UUID uuid;
  private String uri;
  private User user;
  private Submission submission;
  
  @Before
  public void setUp() {
    uuid = UUID.randomUUID();
    uri = "/submission-result";
    user = new User();
    submission = new Submission();
    submission.setId(uuid);
    submission.setStatus(SubmissionStatus.PENDING);
  }
  
  @Test
  public void resultStatusFailedWhenTestTaskInSubmission() {
    driveTaskTypeExpectationTest(TaskType.TEST, "TESTS_FAILED");
  }
  
  @Test
  public void resultStatusPassedWhenImplementationTaskInSubmission() {
    driveTaskTypeExpectationTest(TaskType.IMPLEMENTATION, "PASSED");
  }
  
  private void driveTaskTypeExpectationTest(TaskType givenTaskType, String expectedContent) {
    Task task = new Task(0, givenTaskType, user, "name", "desc", "codeStub", 0, 0);
    TaskInstance taskInstance = new TaskInstance(user, "code", task);
    submission.setTaskInstance(taskInstance);
    
    when(submissionRepositoryMock.findById(eq(uuid)))
        .thenReturn(Optional.of(submission));
        
    server.expect(requestTo(uri))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(content().string(containsString(expectedContent)))
        .andRespond(withSuccess());
    
    fakeSandboxRestService.postSubmissionResults(uuid.toString(), uri);
  }
}
