package pingis.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.EditorService;
import pingis.services.logic.GameplayService;
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

  @MockBean
  private GameplayService gameplayServiceMock;

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
        "Implement addition", "public class Test {}", 1, 1);
    implementationTask = new Task(2,
        TaskType.IMPLEMENTATION, testUser, "implement addition",
        "implement addition", "public class Impl {}", 1, 1);
    testTaskInstance
        = new TaskInstance(testUser, "public class Test {}", testTask);
    implTaskInstance = new TaskInstance(testUser, "public class Impl {}",
        implementationTask);
    implTaskInstance.setTestTaskInstance(testTaskInstance);
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
    when(gameplayServiceMock.canPlayOrSkip(any(), any())).thenReturn(true);
    Map<String, EditorTabData> tabData = this
        .generateTabData(implTaskInstance, testTaskInstance);
    when(editorServiceMock.generateEditorContents(testTaskInstance)).thenReturn(tabData);
    String uri = "/task/" + testTaskInstance.getId();
    performSimpleGetRequestAndFindContent(uri, "task", testTask.getCodeStub());
    verify(taskInstanceServiceMock, times(1)).findOne(testTaskInstance.getId());
    verify(gameplayServiceMock).canPlayOrSkip(any(), any());
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
    when(gameplayServiceMock.canPlayOrSkip(any(), any())).thenReturn(true);

    Map<String, EditorTabData> tabData = generateTabData(implTaskInstance, testTaskInstance);
    when(editorServiceMock.generateEditorContents(implTaskInstance)).thenReturn(tabData);
    String uri = "/task/" + implTaskInstance.getId();
    performSimpleGetRequestAndFindContent(uri, "task", implementationTask.getCodeStub());
    verify(taskInstanceServiceMock, times(1)).findOne(implTaskInstance.getId());
    verify(gameplayServiceMock).canPlayOrSkip(any(), any());
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
    tabData.put("impl", tab1);
    tabData.put("test", tab2);
    return tabData;
  }

  @Test
  public void submitTestTask() throws Exception {
    String submissionCode = "public class Task {}";

    when(taskInstanceServiceMock.findOne(testTaskInstance.getId())).thenReturn(testTaskInstance);
    when(challengeServiceMock.findOne(challenge.getId())).thenReturn(challenge);
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
    String submissionCode = "public class Impl {}";

    when(taskInstanceServiceMock.findOne(implTaskInstance.getId())).thenReturn(implTaskInstance);
    when(challengeServiceMock.findOne(challenge.getId())).thenReturn(challenge);
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
    verifyNoMoreInteractions(taskInstanceServiceMock);
    verifyNoMoreInteractions(challengeServiceMock);
    verifyNoMoreInteractions(taskServiceMock);
  }

  @Test
  public void givenErrorWhenGetNonExistentTask() throws Exception {
    when(taskInstanceServiceMock.findOne(123))
        .thenReturn(null);
    mvc.perform(get("/task/123"))
        .andExpect(status().isOk())
        .andExpect(view().name("error"));
    verify(taskInstanceServiceMock, times(1)).findOne(123);
    verifyNoMoreInteractions(taskInstanceServiceMock);
    verifyNoMoreInteractions(editorServiceMock);
  }

  @Test
  public void correctFileNamesAreSubmitted() throws Exception {
    String submissionCode = "public class Sample {}";

    Challenge challenge = mock(Challenge.class);
    when(challenge.getId()).thenReturn(1L);

    Task task = mock(Task.class);
    when(task.getChallenge()).thenReturn(challenge);
    when(task.getType()).thenReturn(TaskType.TEST);
    when(task.getCodeStub()).thenReturn(submissionCode);

    TaskInstance taskInstance = mock(TaskInstance.class);
    when(taskInstance.getTask()).thenReturn(task);

    Submission submission = mock(Submission.class);
    when(submission.getId()).thenReturn(UUID.randomUUID());

    when(taskInstanceServiceMock.findOne(anyLong())).thenReturn(taskInstance);
    when(taskServiceMock.getCorrespondingTask(any())).thenReturn(task);
    when(sandboxServiceMock.submit(any(), any())).thenReturn(submission);

    mvc.perform(post("/task")
          .param("submissionCode", submissionCode)
          .param("taskInstanceId", Long.toString(0)))
          .andExpect(status().is3xxRedirection());

    verify(sandboxServiceMock).submit(packagingArgCaptor.capture(), any());

    Map<String, byte[]> map = packagingArgCaptor.getValue();
    assertTrue(map.containsKey("src/Sample.java"));
    assertTrue(map.containsKey("test/SampleTest.java"));
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
