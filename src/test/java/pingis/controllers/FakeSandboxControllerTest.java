package pingis.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pingis.entities.sandbox.RequestedStatus.COMPILE_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TASK_PASSING;
import static pingis.entities.sandbox.RequestedStatus.TESTS_FAILED;
import static pingis.entities.sandbox.RequestedStatus.TESTS_PASSED;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pingis.config.OAuthProperties;
import pingis.config.SecurityConfig;
import pingis.entities.sandbox.RequestedStatus;
import pingis.services.sandbox.FakeSandboxRestService;

/**
 *
 * @author juicyp
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    FakeSandboxController.class, SecurityConfig.class, OAuthProperties.class})
@WebAppConfiguration
@WebMvcTest(FakeSandboxController.class)
public class FakeSandboxControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private FakeSandboxRestService fakeSandboxMock;

  private UUID uuid;
  private String url;

  @Before
  public void setup() {
    uuid = UUID.randomUUID();
    url = "localhost";
  }

  @Test
  public void postsTestsPassedWhenRequested() throws Exception {
    postRequestedStatusAndVerify(TESTS_PASSED);
  }

  @Test
  public void postsTestsFailedWhenRequested() throws Exception {
    postRequestedStatusAndVerify(TESTS_FAILED);
  }

  @Test
  public void postsCompileFailedWhenRequested() throws Exception {
    postRequestedStatusAndVerify(COMPILE_FAILED);
  }

  @Test
  public void postsTaskPassingWhenRequested() throws Exception {
    postRequestedStatusAndVerify(TASK_PASSING);
  }

  @Test
  public void badRequestWithInvalidFile() throws Exception {
    mvc.perform(multipart("/tasks.json")
        .file("file", "any".getBytes())
        .param("token", uuid.toString())
        .param("notify", url))
        .andExpect(status().isBadRequest());
  }

  private void postRequestedStatusAndVerify(RequestedStatus request) throws Exception {
    mvc.perform(multipart("/tasks.json")
        .file("file", request.asBytes())
        .param("token", uuid.toString())
        .param("notify", url))
        .andExpect(status().isOk());

    verify(fakeSandboxMock).postSubmissionResults(
            uuid.toString(), url, request);
  }
}
