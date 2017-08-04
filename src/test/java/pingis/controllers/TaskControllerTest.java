package pingis.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pingis.config.SecurityDevConfig;
import pingis.entities.Challenge;
import pingis.entities.ImplementationType;
import pingis.entities.Task;
import pingis.entities.User;
import pingis.services.*;
import pingis.utils.JavaClassGenerator;

import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = {TaskController.class, SecurityDevConfig.class})
@WebAppConfiguration
public class TaskControllerTest {

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private SubmissionPackagingService packagingService;

    @MockBean
    private SubmissionSenderService senderService;

    @MockBean
    private ChallengeService challengeServiceMock;

    @MockBean
    private TaskService taskServiceMock;

    // Never accessed but necessary for testing
    @MockBean
    private EditorService editorServiceMock;

    @Captor
    private ArgumentCaptor<Map<String, byte[]>> packagingArgCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void submitTask() throws Exception {
        User testUser = new User(1, "TESTUSER", 1);
        Challenge challenge = new Challenge("Calculator", testUser, "Simple calculator");
        Task task = new Task(1, ImplementationType.TEST, testUser,
                "CalculatorAddition","Implement addition", "return 1+1;", 1, 1);

        String implFileName = JavaClassGenerator.generateImplClassFilename(challenge);
        String implCode = "/* this is an implementation */";
        String testFileName = JavaClassGenerator.generateTestClassFilename(challenge);
        String testCode = "/* this is a test */";

        when(challengeServiceMock.findOne(challenge.getId())).thenReturn(challenge);
        when(taskServiceMock.findTaskInChallenge(challenge.getId(), task.getTaskId())).thenReturn(task);

        mvc.perform(post("/task")
                .param("implementationCode", implCode)
                .param("testCode", testCode)
                .param("challengeId", Long.toString(challenge.getId()))
                .param("taskId", Long.toString(task.getTaskId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/feedback*"));

        verify(packagingService).packageSubmission(packagingArgCaptor.capture());

        Map<String, byte[]> files = packagingArgCaptor.getValue();
        assertArrayEquals(implCode.getBytes(), files.get(implFileName));
        assertArrayEquals(testCode.getBytes(), files.get(testFileName));

        verifyNoMoreInteractions(packagingService);
    }

    @Test
    public void givenTaskWhenGetTask() throws Exception {
        User testUser = new User(1, "TESTUSER", 1);
        Challenge calculatorChallenge = new Challenge("Calculator",
                testUser,
                "Simple calculator");
        Task simpleCalculatortask = new Task(
                1,
                ImplementationType.TEST,
                testUser,
                "CalculatorAddition",
                "Implement addition",
                "return 1+1;",
                1,
                1);

        when(challengeServiceMock.findOne(calculatorChallenge.getId())).thenReturn(calculatorChallenge);
        when(taskServiceMock.findTaskInChallenge(calculatorChallenge.getId(),
                simpleCalculatortask.getTaskId()-1))
                .thenReturn(simpleCalculatortask);

        String uri = "/task/"+calculatorChallenge.getId()+"/"+simpleCalculatortask.getId();

        performSimpleGetRequestAndFindContent(uri, "task", simpleCalculatortask.getDesc());

        verify(challengeServiceMock, times(1)).findOne(calculatorChallenge.getId());
        verify(taskServiceMock, times(1))
                .findTaskInChallenge(calculatorChallenge.getId(), simpleCalculatortask.getTaskId()-1);
        verifyNoMoreInteractions(challengeServiceMock);
        verifyNoMoreInteractions(taskServiceMock);
    }

    @Test
    public void givenFeedbackWhenGetFeedback() throws Exception {
        performSimpleGetRequestAndFindContent("/feedback", "feedback", "<h1>Feedback</h1>");
    }


    private void performSimpleGetRequestAndFindContent(String uri,
                                                       String viewName,
                                                       String expectedContent) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name(viewName))
                .andExpect(content().string(containsString(expectedContent)));
    }
}

