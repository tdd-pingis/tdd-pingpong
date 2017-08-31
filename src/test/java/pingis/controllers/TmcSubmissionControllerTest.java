package pingis.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pingis.Application;
import pingis.config.OAuthProperties;
import pingis.config.SecurityConfig;
import pingis.config.WebSocketSecurityConfig;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.sandbox.Logs;
import pingis.entities.sandbox.ResultStatus;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.entities.sandbox.TestOutput;
import pingis.services.entity.TaskInstanceService;
import pingis.services.sandbox.SandboxService;

/**
 * Created by dwarfcrank on 7/28/17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes =
    {Application.class, TmcSubmissionController.class, SecurityConfig.class, OAuthProperties.class,
        WebSocketSecurityConfig.class})
@DirtiesContext
@WebAppConfiguration
@WebMvcTest(TmcSubmissionController.class)
public class TmcSubmissionControllerTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private SandboxService sandboxServiceMock;

  @MockBean
  private TaskInstanceService taskInstanceServiceMock;

  private String testOutput;

  // TODO: Make this generate random data. This is a separate method just to please checkstyle.
  private ResultActions performMockRequest(UUID submissionId) throws Exception {
    return mvc.perform(
        post("/submission-result")
            .param("test_output", testOutput)
            .param("stdout", "test_stdout")
            .param("stderr", "test_stderr")
            .param("validations", "test_validations")
            .param("vm_log", "test_vm_log")
            .param("token", submissionId.toString())
            .param("status", "finished")
            .param("exit_code", "0"));
  }

  @Before
  public void initializeTestOutput() throws JsonProcessingException {
    TestOutput top = new TestOutput();
    top.setStatus(ResultStatus.PASSED);
    top.setTestResults(new ArrayList<>());

    Logs logs = new Logs();
    logs.setStderr("stderr".getBytes());
    logs.setStdout("stdout".getBytes());
    top.setLogs(logs);

    ObjectMapper mapper = new ObjectMapper();

    this.testOutput = mapper.writeValueAsString(top);
  }

  @Test
  public void doubleSubmitReturnsNotFound() throws Exception {
    UUID submissionId = UUID.randomUUID();
    Submission submission = createSubmission();
    submission.setId(submissionId);

    when(sandboxServiceMock.updateSubmissionResult(eq(submissionId), anyString(), anyString(),
        anyString(), anyString(), anyString(), anyString(), anyInt()))
        .thenReturn(true)
        .thenReturn(false);

    performMockRequest(submissionId)
        .andExpect(status().isOk());

    performMockRequest(submissionId)
        .andExpect(status().isNotFound());

    verify(sandboxServiceMock, times(2))
        .updateSubmissionResult(eq(submissionId), anyString(), anyString(), anyString(),
            anyString(), anyString(), anyString(), anyInt());
    verifyNoMoreInteractions(sandboxServiceMock);
  }

  @Test
  public void returnsNotFoundWithInvalidToken() throws Exception {
    UUID submissionId = UUID.randomUUID();
    Submission submission = createSubmission();
    submission.setId(submissionId);

    when(sandboxServiceMock.updateSubmissionResult(eq(submissionId), anyString(), anyString(),
        anyString(), anyString(), anyString(), anyString(), anyInt()))
        .thenReturn(false);

    performMockRequest(submissionId)
        .andExpect(status().isNotFound());

    verify(sandboxServiceMock).updateSubmissionResult(eq(submissionId), anyString(), anyString(),
        anyString(), anyString(), anyString(), anyString(), anyInt());

    verifyNoMoreInteractions(sandboxServiceMock);
  }

  @Test
  public void testWithValidToken() throws Exception {
    UUID submissionId = UUID.randomUUID();
    Submission submission = createSubmission();
    submission.setId(submissionId);

    when(sandboxServiceMock.updateSubmissionResult(eq(submissionId), anyString(), anyString(),
        anyString(), anyString(), anyString(), anyString(), anyInt()))
        .thenReturn(true);

    performMockRequest(submissionId)
        .andExpect(status().isOk());

    verify(sandboxServiceMock).updateSubmissionResult(eq(submissionId), anyString(), anyString(),
        anyString(), anyString(), anyString(), anyString(), anyInt());

    verifyNoMoreInteractions(sandboxServiceMock);
  }

  private Submission createSubmission() {
    Submission submission = new Submission();

    submission.setStatus(SubmissionStatus.PENDING);

    Task task = new Task(0, null, null, null, null, null, 0, 0);
    TaskInstance ti = new TaskInstance(null, null, task);
    submission.setTaskInstance(ti);
    return submission;
  }

  private void assertSubmission(Submission captured, UUID submissionId) {
    assertEquals((int) captured.getExitCode(), 0);
    assertEquals(captured.getStdout(), "test_stdout");
    assertEquals(captured.getStderr(), "test_stderr");
    assertEquals(captured.getValidations(), "test_validations");
    assertEquals(captured.getVmLog(), "test_vm_log");
    assertEquals(captured.getStatus(), SubmissionStatus.FINISHED);
    assertEquals(captured.getId(), submissionId);

    TestOutput top = captured.getTestOutput();
    assertEquals(top.getStatus(), ResultStatus.PASSED);
    assertNotNull(top.getTestResults());
    assertEquals("stderr", new String(top.getLogs().getStderr()));
    assertEquals("stdout", new String(top.getLogs().getStdout()));
  }
}
