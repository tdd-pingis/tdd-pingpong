package pingis.services.sandbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.sandbox.Logs;
import pingis.entities.sandbox.ResultStatus;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.entities.sandbox.TestOutput;
import pingis.repositories.sandbox.SubmissionRepository;
import pingis.services.TaskInstanceService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SandboxService.class})
public class SandboxServiceTest {

  @MockBean
  private TaskInstanceService taskInstanceService;

  @MockBean
  private SubmissionSenderService senderService;

  @MockBean
  private SubmissionPackagingService packagingService;

  @MockBean
  private SubmissionRepository submissionRepository;

  @MockBean
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private SandboxService sandboxService;

  private Submission submission;
  private UUID submissionId;
  private ArgumentCaptor<Submission> submissionArgumentCaptor;

  private String generateTestOutput(ResultStatus status) throws JsonProcessingException {
    TestOutput top = new TestOutput();
    top.setStatus(status);
    top.setTestResults(new ArrayList<>());

    Logs logs = new Logs();
    logs.setStderr("stderr".getBytes());
    logs.setStdout("stdout".getBytes());
    top.setLogs(logs);

    return new ObjectMapper().writeValueAsString(top);
  }

  private void generateSubmission() {
    submission = new Submission();
    submissionId = UUID.randomUUID();

    submission.setId(submissionId);
    submission.setStatus(SubmissionStatus.PENDING);

    TaskInstance taskInstance = new TaskInstance(null, "code",
        new Task(0, TaskType.TEST, null, "name", "desc",
            "code", 0, 0));
    submission.setTaskInstance(taskInstance);
  }

  @Before
  public void setup() {
    generateSubmission();
    submissionArgumentCaptor = ArgumentCaptor.forClass(Submission.class);
  }

  private boolean updateSubmission(UUID submissionId, ResultStatus status) throws IOException {
    String testOutput = generateTestOutput(status);

    return sandboxService.updateSubmissionResult(submissionId, testOutput,
        "stdout", "stderr", "validations", "vmLog",
        "FINISHED", 0);
  }

  private void checkSubmission(UUID submissionId, Submission actual) {
    assertEquals("stdout", actual.getStdout());
    assertEquals("stderr", actual.getStderr());
    assertEquals("validations", actual.getValidations());
    assertEquals("vmLog", actual.getVmLog());
    assertEquals(SubmissionStatus.FINISHED, actual.getStatus());
    assertEquals(0, (int) actual.getExitCode());
    assertEquals(submissionId, actual.getId());
  }

  @Test
  public void doubleUpdateResult() throws Exception {
    when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));

    boolean firstResult = updateSubmission(submissionId, ResultStatus.PASSED);
    boolean secondResult = updateSubmission(submissionId, ResultStatus.PASSED);

    verify(submissionRepository).save(any(Submission.class));

    assertTrue(firstResult);
    assertFalse(secondResult);

    verify(submissionRepository, times(2)).findById(submissionId);
    verifyNoMoreInteractions(submissionRepository);
  }

  @Test
  public void nonExistentUpdateResult() throws Exception {
    when(submissionRepository.findById(submissionId)).thenReturn(Optional.empty());

    boolean result = updateSubmission(submissionId, null);

    assertFalse(result);
    verify(submissionRepository, times(1)).findById(submissionId);
    verifyNoMoreInteractions(submissionRepository);
  }

  @Test
  public void successfullyUpdateResult() throws Exception {
    when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));

    boolean result = updateSubmission(submissionId, ResultStatus.TESTS_FAILED);

    verify(submissionRepository).save(submissionArgumentCaptor.capture());

    assertTrue(result);
    checkSubmission(submissionId, submissionArgumentCaptor.getValue());

    verify(submissionRepository).findById(submissionId);
    verify(taskInstanceService).markAsDone(submission.getTaskInstance());
    verifyNoMoreInteractions(submissionRepository, taskInstanceService);
  }

  @Test
  public void updateResultWithTestCompileFailure() throws Exception {
    when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));

    boolean result = updateSubmission(submissionId, ResultStatus.COMPILE_FAILED);

    verify(submissionRepository).save(submissionArgumentCaptor.capture());

    assertTrue(result);
    checkSubmission(submissionId, submissionArgumentCaptor.getValue());

    verify(submissionRepository).findById(submissionId);
    verify(taskInstanceService).markAsDone(any());
    verifyNoMoreInteractions(submissionRepository, taskInstanceService);
  }

  @Test
  public void updateResultWithTestFailure() throws Exception {
    // Tests failing in implementation task == failure
    submission.getTaskInstance().getTask().setType(TaskType.IMPLEMENTATION);

    when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));

    boolean result = updateSubmission(submissionId, ResultStatus.TESTS_FAILED);

    verify(submissionRepository).save(submissionArgumentCaptor.capture());

    assertTrue(result);
    checkSubmission(submissionId, submissionArgumentCaptor.getValue());

    verify(submissionRepository).findById(submissionId);
    verifyNoMoreInteractions(submissionRepository, taskInstanceService);
  }

  @Test
  public void submitSuccessful() throws Exception {
    SubmissionResponse response = new SubmissionResponse();
    response.setStatus(SubmissionResponse.OK);

    when(senderService.sendSubmission(any(), any()))
        .thenReturn(CompletableFuture.completedFuture(response));

    Submission submission = sandboxService.submit(null, null);

    assertNotNull(submission);
    assertEquals(SubmissionStatus.PENDING, submission.getStatus());

    verify(submissionRepository, times(2)).save(any(Submission.class));
    verify(senderService).sendSubmission(any(), any());
    verifyNoMoreInteractions(senderService, submissionRepository);
  }

  @Test
  public void submitUnsuccessfulWithException() throws Exception {
    CompletableFuture<SubmissionResponse> future = new CompletableFuture<>();
    future.completeExceptionally(new Exception());

    when(senderService.sendSubmission(any(), any()))
        .thenReturn(future);

    Submission submission = sandboxService.submit(null, null);

    assertNotNull(submission);
    assertEquals(SubmissionStatus.SEND_FAILED, submission.getStatus());

    verify(submissionRepository, times(2)).save(any(Submission.class));
    verify(senderService).sendSubmission(any(), any());
    verifyNoMoreInteractions(senderService, submissionRepository);
  }

  @Test
  public void submitUnsuccessful() throws Exception {
    SubmissionResponse response = new SubmissionResponse();
    response.setStatus(SubmissionResponse.BAD_REQUEST);

    when(senderService.sendSubmission(any(), any()))
        .thenReturn(CompletableFuture.completedFuture(response));

    Submission submission = sandboxService.submit(null, null);

    assertNotNull(submission);
    assertEquals(SubmissionStatus.SEND_FAILED, submission.getStatus());

    verify(submissionRepository, times(2)).save(any(Submission.class));
    verify(senderService).sendSubmission(any(), any());
    verifyNoMoreInteractions(senderService, submissionRepository);
  }
}
