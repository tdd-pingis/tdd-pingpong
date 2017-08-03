package pingis.controllers;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pingis.config.SecurityDevConfig;
import pingis.entities.*;
import pingis.services.ChallengeService;
import pingis.services.EditorService;
import pingis.services.TaskService;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private ChallengeService challengeServiceMock;

    @MockBean
    private TaskService taskServiceMock;

    // Never accessed but necessary for testing
    @MockBean
    private EditorService editorServiceMock;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
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

