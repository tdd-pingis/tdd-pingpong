package pingis.controllers;

import java.util.LinkedHashMap;
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
import pingis.entities.ChallengeImplementation;
import pingis.entities.TaskImplementation;
import pingis.utils.EditorTabData;


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
    private TaskImplementationService taskImplementationServiceMock;
            
    @MockBean
    private TaskService taskServiceMock;


    @MockBean
    private EditorService editorServiceMock;

    @Captor
    private ArgumentCaptor<Map<String, byte[]>> packagingArgCaptor;
    
    private Challenge challenge;
    private Task testTask;
    private Task implementationTask;
    private ChallengeImplementation testChallengeImplementation;
    private TaskImplementation testTaskImplementation;
    private TaskImplementation implTaskImplementation;
    private User testUser;

    @Before
    public void setUp() {
        testUser = new User(1, "TESTUSER", 1);
        challenge = new Challenge("Calculator", testUser,
                "Simple calculator");
        testTask = new Task(1,
                ImplementationType.TEST, testUser, "CalculatorAddition",
                "Implement addition", "return 1+1;", 1, 1);
        implementationTask = new Task(2,
                ImplementationType.IMPLEMENTATION, testUser, "implement addition",
                "implement addition", "public test", 1, 1);
        testChallengeImplementation
                = new ChallengeImplementation(challenge, testUser, testUser);
        testTaskImplementation
                = new TaskImplementation(testUser, "", testTask);
        testTaskImplementation.setChallengeImplementation(testChallengeImplementation);
        implTaskImplementation = new TaskImplementation(testUser, "",
                implementationTask);
        implTaskImplementation.setChallengeImplementation(testChallengeImplementation);
        challenge.addTask(implementationTask);
        implementationTask.setChallenge(challenge);
        MockitoAnnotations.initMocks(this);

        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }



    @Test
    public void givenTaskWhenGetTestTask() throws Exception {
        when(taskImplementationServiceMock.findOne(testTaskImplementation.getId()))
                .thenReturn(testTaskImplementation);
        Map<String, EditorTabData> tabData = this.generateTestTabData(implTaskImplementation, testTaskImplementation);
        when(editorServiceMock.generateEditorContents(testTaskImplementation)).thenReturn(tabData);
        String uri = "/task/"+testTaskImplementation.getId();
        performSimpleGetRequestAndFindContent(uri, "task", testTask.getCodeStub());
        verify(taskImplementationServiceMock, times(1)).findOne(testTaskImplementation.getId());
        verify(editorServiceMock, times(1))
                .generateEditorContents(testTaskImplementation);
        verifyNoMoreInteractions(taskImplementationServiceMock);
        verifyNoMoreInteractions(editorServiceMock);
    }
    
    @Test
    public void givenTaskWhenGetImplementationTask() throws Exception {
        User testUser = new User(1, "TESTUSER", 1);

        when(taskImplementationServiceMock.findOne(implTaskImplementation.getId()))
                .thenReturn(implTaskImplementation);
        Map<String, EditorTabData> tabData = generateImplTabData(implTaskImplementation, testTaskImplementation);
        when(editorServiceMock.generateEditorContents(implTaskImplementation)).thenReturn(tabData);
        String uri = "/task/" + implTaskImplementation.getId();
        performSimpleGetRequestAndFindContent(uri, "task", implementationTask.getCodeStub());
        verify(taskImplementationServiceMock, times(1)).findOne(implTaskImplementation.getId());
        verify(editorServiceMock, times(1))
                .generateEditorContents(implTaskImplementation);
        verifyNoMoreInteractions(taskImplementationServiceMock);
        verifyNoMoreInteractions(editorServiceMock);
    }
    
    private Map<String, EditorTabData> generateImplTabData(TaskImplementation implTaskImplementation,
            TaskImplementation testTaskImplementation) {
        Map<String, EditorTabData> tabData = new LinkedHashMap<String, EditorTabData>();
        EditorTabData tab1 = new EditorTabData("Implement code here",
                        implTaskImplementation.getTask().getCodeStub());
        EditorTabData tab2 = new EditorTabData("Test to fulfill",testTaskImplementation.getTask().getCodeStub());
        tabData.put("editor1", tab1);
        tabData.put("editor2", tab2);
        return tabData;
    }
  
    private Map<String, EditorTabData> generateTestTabData(TaskImplementation implTaskImplementation,
            TaskImplementation testTaskImplementation) {
        Map<String, EditorTabData> tabData = new LinkedHashMap<String, EditorTabData>();
        EditorTabData tab1 = new EditorTabData("Implement code here",
                implTaskImplementation.getTask().getCodeStub());
        EditorTabData tab2 = new EditorTabData("Test to fulfill", testTaskImplementation.getTask().getCodeStub());
        tabData.put("editor1", tab1);
        tabData.put("editor2", tab2);
        return tabData;
    }

    @Test
    public void submitTask() throws Exception {
        String submissionFileName = JavaClassGenerator.generateImplClassFilename(challenge);
        String submissionCode = "/* this is an implementation */";
        String staticFileName = JavaClassGenerator.generateTestClassFilename(challenge);
        String staticCode = "/* this is a test */";
        when(taskImplementationServiceMock.findOne(implTaskImplementation.getId())).thenReturn(implTaskImplementation);
        when(challengeServiceMock.findOne(challenge.getId())).thenReturn(challenge);
        when(taskServiceMock.findTaskInChallenge(challenge.getId(), testTask.getIndex())).thenReturn(testTask);
        mvc.perform(post("/task")
                .param("submissionCode", submissionCode)
                .param("staticCode", staticCode)
                .param("taskImplementationId", Long.toString(implTaskImplementation.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/feedback*"));
        verify(packagingService).packageSubmission(packagingArgCaptor.capture());

        Map<String, byte[]> files = packagingArgCaptor.getValue();
        assertArrayEquals(submissionCode.getBytes(), files.get(submissionFileName));
        assertArrayEquals(staticCode.getBytes(), files.get(staticFileName));

        verify(taskImplementationServiceMock, times(1)).findOne(implTaskImplementation.getId());
        verify(taskImplementationServiceMock).updateTaskImplementationCode(implTaskImplementation.getId(), submissionCode);

        verifyNoMoreInteractions(packagingService);
        verifyNoMoreInteractions(taskImplementationServiceMock);
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


