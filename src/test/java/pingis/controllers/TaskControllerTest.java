package pingis.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
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
import pingis.config.OAuthProperties;
import pingis.config.SecurityConfig;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.entities.sandbox.Submission;
import pingis.services.ChallengeService;
import pingis.services.EditorService;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;
import pingis.services.UserService;
import pingis.services.sandbox.SandboxService;
import pingis.utils.EditorTabData;


@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = {TaskController.class, SecurityConfig.class, OAuthProperties.class})
@WebAppConfiguration
public class TaskControllerTest {

  @Autowired
  WebApplicationContext context;

  private MockMvc mvc;

  @MockBean
  private SandboxService sandboxServiceMock;

  @MockBean
  private ChallengeService challengeServiceMock;

  @MockBean
  private TaskInstanceService taskInstanceServiceMock;

  @MockBean
  private TaskService taskServiceMock;

  @MockBean
  private EditorService editorServiceMock;

  @MockBean
  private UserService userServiceMock;

  @Captor
  private ArgumentCaptor<Map<String, byte[]>> packagingArgCaptor;

  private Challenge challenge;
  private Task testTask;
  private Task implementationTask;
  private TaskInstance testTaskInstance;
  private TaskInstance implTaskInstance;
  private User testUser;
  private Submission submission;

  @Before
  public void setUp() {
    testUser = new User(1, "TESTUSER", 1);
    challenge = new Challenge("Calculator", testUser,
        "Simple calculator");
    testTask = new Task(1,
        TaskType.TEST, testUser, "CalculatorAddition",
        "Implement addition", "return 1+1;", 1, 1);
    implementationTask = new Task(2,
        TaskType.IMPLEMENTATION, testUser, "implement addition",
        "implement addition", "public test", 1, 1);
    testTaskInstance
        = new TaskInstance(testUser, "", testTask);
    implTaskInstance = new TaskInstance(testUser, "",
        implementationTask);
    testTask.setChallenge(challenge);
    challenge.addTask(implementationTask);
    implementationTask.setChallenge(challenge);
    submission = new Submission();
    submission.setId(UUID.randomUUID());
    MockitoAnnotations.initMocks(this);

    this.mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();
  }


  @Test
  public void givenTaskWhenGetTestTask() throws Exception {
    when(taskInstanceServiceMock.findOne(testTaskInstance.getId()))
        .thenReturn(testTaskInstance);
    Map<String, EditorTabData> tabData = this
        .generateTabData(implTaskInstance, testTaskInstance);
    when(editorServiceMock.generateEditorContents(testTaskInstance)).thenReturn(tabData);
    String uri = "/task/" + testTaskInstance.getId();
    performSimpleGetRequestAndFindContent(uri, "task", testTask.getCodeStub());
    verify(taskInstanceServiceMock, times(1)).findOne(testTaskInstance.getId());
    verify(editorServiceMock, times(1))
        .generateEditorContents(testTaskInstance);
    verifyNoMoreInteractions(taskInstanceServiceMock);
    verifyNoMoreInteractions(editorServiceMock);
  }

  @Test
  public void givenTaskWhenGetImplementationTask() throws Exception {
    User testUser = new User(1, "TESTUSER", 1);

    when(taskInstanceServiceMock.findOne(implTaskInstance.getId()))
        .thenReturn(implTaskInstance);
    Map<String, EditorTabData> tabData = generateTabData(implTaskInstance, testTaskInstance);
    when(editorServiceMock.generateEditorContents(implTaskInstance)).thenReturn(tabData);
    String uri = "/task/" + implTaskInstance.getId();
    performSimpleGetRequestAndFindContent(uri, "task", implementationTask.getCodeStub());
    verify(taskInstanceServiceMock, times(1)).findOne(implTaskInstance.getId());
    verify(editorServiceMock, times(1))
        .generateEditorContents(implTaskInstance);
    verifyNoMoreInteractions(taskInstanceServiceMock);
    verifyNoMoreInteractions(editorServiceMock);
  }

  private Map<String, EditorTabData> generateTabData(TaskInstance implTaskInstance,
      TaskInstance testTaskInstance) {
    Map<String, EditorTabData> tabData = new LinkedHashMap<String, EditorTabData>();
    EditorTabData tab1 = new EditorTabData("Implement code here",
        implTaskInstance.getTask().getCodeStub());
    EditorTabData tab2 = new EditorTabData("Test to fulfill",
        testTaskInstance.getTask().getCodeStub());
    tabData.put("editor1", tab1);
    tabData.put("editor2", tab2);
    return tabData;
  }

  @Test
  public void submitTestTask() throws Exception {
    String submissionCode = "/* this is a test */";

    when(taskInstanceServiceMock.findOne(testTaskInstance.getId())).thenReturn(testTaskInstance);
    when(challengeServiceMock.findOne(challenge.getId())).thenReturn(challenge);
    when(taskServiceMock.findTaskInChallenge(challenge.getId(), implementationTask.getIndex()))
        .thenReturn(implementationTask);
    when(taskServiceMock.getCorrespondingTask(testTask)).thenReturn(implementationTask);
    when(sandboxServiceMock.submit(Mockito.any(), Mockito.any())).thenReturn(submission);
    mvc.perform(post("/task")
        .param("submissionCode", submissionCode)
        .param("taskInstanceId", Long.toString(implTaskInstance.getId())))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/feedback*"));

    verify(taskInstanceServiceMock, times(1)).findOne(testTaskInstance.getId());
    verify(taskInstanceServiceMock).updateTaskInstanceCode(testTaskInstance.getId(),
        submissionCode);
    verify(taskServiceMock).getCorrespondingTask(testTask);
    verifyNoMoreInteractions(taskInstanceServiceMock);
    verifyNoMoreInteractions(challengeServiceMock);
    verifyNoMoreInteractions(taskServiceMock);
  }

  @Test
  public void submitImplementationTask() throws Exception {
    String submissionCode = "/* this is an implementation */";

    when(taskInstanceServiceMock.findOne(implTaskInstance.getId())).thenReturn(implTaskInstance);
    when(challengeServiceMock.findOne(challenge.getId())).thenReturn(challenge);
    when(taskServiceMock.findTaskInChallenge(challenge.getId(), testTask.getIndex()))
        .thenReturn(testTask);
    when(taskServiceMock.getCorrespondingTask(implementationTask)).thenReturn(testTask);
    when(sandboxServiceMock.submit(Mockito.any(), Mockito.any())).thenReturn(submission);
    mvc.perform(post("/task")
        .param("submissionCode", submissionCode)
        .param("taskInstanceId", Long.toString(implTaskInstance.getId())))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/feedback*"));

    verify(taskInstanceServiceMock, times(1)).findOne(implTaskInstance.getId());
    verify(taskInstanceServiceMock).updateTaskInstanceCode(implTaskInstance.getId(),
        submissionCode);
    verify(taskServiceMock).getCorrespondingTask(implementationTask);
    verifyNoMoreInteractions(taskInstanceServiceMock);
    verifyNoMoreInteractions(challengeServiceMock);
    verifyNoMoreInteractions(taskServiceMock);
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


