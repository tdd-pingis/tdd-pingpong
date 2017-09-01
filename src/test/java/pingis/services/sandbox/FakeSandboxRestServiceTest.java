package pingis.services.sandbox;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static pingis.entities.sandbox.RequestedStatus.COMPILE_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TASK_PASSING;
import static pingis.entities.sandbox.RequestedStatus.TESTS_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TESTS_PASSED;

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
import pingis.entities.sandbox.RequestedStatus;
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
  private Submission submission;

  @Before
  public void setUp() {
    uuid = UUID.randomUUID();
    uri = "/submission-result";
    submission = mock(Submission.class);
    when(submission.getId()).thenReturn(uuid);
    when(submission.getStatus()).thenReturn(SubmissionStatus.PENDING);
  }

  @Test
  public void resultStatusFailedWhenRequestingTestTaskPassing() {
    driveTaskTypeExpectationTest(TaskType.TEST, TASK_PASSING, "TESTS_FAILED");
  }

  @Test
  public void resultStatusPassedWhenRequestingImplementationTaskPassing() {
    driveTaskTypeExpectationTest(TaskType.IMPLEMENTATION, TASK_PASSING, "PASSED");
  }

  @Test
  public void resultStatusPassedWhenRequested() {
    driveTaskTypeExpectationTest(TaskType.IMPLEMENTATION, TESTS_PASSED, "PASSED");
  }

  @Test
  public void resultStatusTestsFailedWhenRequested() {
    driveTaskTypeExpectationTest(TaskType.IMPLEMENTATION, TESTS_FAILED, "TESTS_FAILED");
  }

  @Test
  public void resultStatusCompileFailedWhenRequested() {
    driveTaskTypeExpectationTest(TaskType.IMPLEMENTATION, COMPILE_FAILED, "COMPILE_FAILED");
  }

  private void driveTaskTypeExpectationTest(
          TaskType givenTaskType, RequestedStatus requestedStatus, String expectedContent) {

    Task task = mock(Task.class);
    when(task.getType()).thenReturn(givenTaskType);

    TaskInstance taskInstance = mock(TaskInstance.class);
    when(taskInstance.getTask()).thenReturn(task);

    when(submission.getTaskInstance()).thenReturn(taskInstance);

    when(submissionRepositoryMock.findById(eq(uuid)))
        .thenReturn(Optional.of(submission));

    server.expect(requestTo(uri))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(content().string(containsString(expectedContent)))
        .andRespond(withSuccess());

    fakeSandboxRestService.postSubmissionResults(
            uuid.toString(), uri, requestedStatus);
  }
}
