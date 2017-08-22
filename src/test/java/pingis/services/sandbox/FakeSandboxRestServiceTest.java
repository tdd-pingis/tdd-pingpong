package pingis.services.sandbox;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.repositories.sandbox.SubmissionRepository;

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
