package pingis.controllers;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pingis.config.SecurityDevConfig;
import pingis.repositories.sandbox.SubmissionRepository;

/**
 *
 * @author juicyp
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {FakeSandboxController.class, SecurityDevConfig.class})
public class FakeSandoxControllerTest {
  
  @Autowired
  private MockMvc mvc;
  
  @MockBean
  private SubmissionRepository submissionRepositoryMock;
  
  @Before
  public void setUp() {
    
  }
}
