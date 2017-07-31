package pingis.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import pingis.config.SecurityConfig;
import pingis.entities.TmcSubmission;
import pingis.entities.TmcSubmissionStatus;
import pingis.repositories.TmcSubmissionRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dwarfcrank on 7/28/17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TmcSubmissionController.class, SecurityConfig.class})
@WebAppConfiguration
@WebMvcTest(TmcSubmissionController.class)
public class TmcSubmissionControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TmcSubmissionRepository submissionRepository;

    // TODO: Make this generate random data. This is a separate method just to please checkstyle.
    private ResultActions performMockRequest(UUID submissionId) throws Exception {
        return mvc.perform(
                post("/submission-result")
                        .param("test_output", "test_output")
                        .param("stdout", "test_stdout")
                        .param("stderr", "test_stderr")
                        .param("validations", "test_validations")
                        .param("vm_log", "test_vm_log")
                        .param("token", submissionId.toString())
                        .param("status", "finished")
                        .param("exit_code", "0"));
    }

    @Test
    public void doubleSubmitReturnsBadRequest() throws Exception {
        UUID submissionId = UUID.randomUUID();
        TmcSubmission submission = new TmcSubmission();

        submission.setStatus(TmcSubmissionStatus.PENDING);
        submission.setId(submissionId);

        given(submissionRepository.findOne(submissionId))
                .willReturn(submission);

        performMockRequest(submissionId)
                .andExpect(status().isOk());

        performMockRequest(submissionId)
                .andExpect(status().isBadRequest());

        ArgumentCaptor<TmcSubmission> submissionCaptor = ArgumentCaptor.forClass(TmcSubmission.class);
        verify(submissionRepository, times(2)).findOne(submissionId);
        verify(submissionRepository, times(1)).save(submissionCaptor.capture());
        verifyNoMoreInteractions(submissionRepository);
    }

    @Test
    public void returnsNotFoundWithInvalidToken() throws Exception {
        UUID submissionId = UUID.randomUUID();

        given(submissionRepository.findOne(submissionId))
                .willReturn(null);

        performMockRequest(submissionId)
                .andExpect(status().isNotFound());

        verify(submissionRepository, times(1)).findOne(submissionId);
        verifyNoMoreInteractions(submissionRepository);
    }

    @Test
    public void testWithValidToken() throws Exception {
        UUID submissionId = UUID.randomUUID();
        TmcSubmission submission = new TmcSubmission();

        submission.setStatus(TmcSubmissionStatus.PENDING);
        submission.setId(submissionId);

        given(submissionRepository.findOne(submissionId))
                .willReturn(submission);

        performMockRequest(submissionId)
                .andExpect(status().isOk());

        ArgumentCaptor<TmcSubmission> submissionCaptor = ArgumentCaptor.forClass(TmcSubmission.class);
        verify(submissionRepository, times(1)).findOne(submissionId);
        verify(submissionRepository, times(1)).save(submissionCaptor.capture());
        verifyNoMoreInteractions(submissionRepository);

        TmcSubmission captured = submissionCaptor.getValue();

        assertEquals((int)captured.getExitCode(), 0);
        assertEquals(captured.getTestOutput(), "test_output");
        assertEquals(captured.getStdout(), "test_stdout");
        assertEquals(captured.getStderr(), "test_stderr");
        assertEquals(captured.getValidations(), "test_validations");
        assertEquals(captured.getVmLog(), "test_vm_log");
        assertEquals(captured.getStatus(), TmcSubmissionStatus.FINISHED);
        assertEquals(captured.getId(), submissionId);

    }
}