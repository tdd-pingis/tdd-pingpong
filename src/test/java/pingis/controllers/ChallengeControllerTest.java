package pingis.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskPair;
import pingis.entities.User;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.ArcadeChallengeService;
import pingis.services.logic.GameplayService;
import pingis.services.logic.GameplayService.TurnType;
import pingis.services.logic.LiveChallengeService;
import pingis.services.logic.PracticeChallengeService;


/**
 * @author authority
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ChallengeController.class)
public class ChallengeControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  ChallengeService challengeService;
  @MockBean
  TaskService taskService;
  @MockBean
  TaskInstanceService taskInstanceService;
  @MockBean
  UserService userService;
  @MockBean
  GameplayService gameplayService;
  @MockBean
  LiveChallengeService liveChallengeService;
  @MockBean
  ArcadeChallengeService arcadeChallengeService;
  @MockBean
  PracticeChallengeService practiceChallengeService;

  @Test
  @WithMockUser
  public void newChallengeReturnsOk() throws Exception {
    mvc.perform(get("/newchallenge")
        .flashAttr("challenge", Mockito.mock(Challenge.class)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  public void cantGetCreateChallenge() throws Exception {
    mvc.perform(get("/createChallenge"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @WithMockUser
  public void creatingChallengeRedirectsToNewTaskPair()
      throws Exception {
    Long challengeId = 123L;

    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(10L);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getId()).thenReturn(challengeId);

    when(userService.getCurrentUser()).thenReturn(user);
    Challenge challengeFromForm = new Challenge("validName", user, "validDesc");
    when(liveChallengeService.createChallenge(challengeFromForm, user))
        .thenReturn(challenge);

    mvc.perform(post("/createChallenge")
        .with(csrf())
        .flashAttr("challenge", challengeFromForm))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/playChallenge/" + challengeId));
  }

  @Test
  @WithMockUser
  public void newTaskpairReturnsNewTaskPairView() throws Exception {
    when(challengeService.findOne(any())).thenReturn(Mockito.mock(Challenge.class));
    mvc.perform(get("/newtaskpair/0"))
        .andExpect(status().isOk())
        .andExpect(view().name("newtaskpair"));
  }

  @Test
  @WithMockUser
  public void createArcadeTaskPairRedirectsToPlayChallenge() throws Exception {
    Long challengeId = 345L;
    Long taskId = 567L;
    Long taskInstanceId = 123L;
    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getName()).thenReturn("haaste");
    when(challenge.getType()).thenReturn(ChallengeType.ARCADE);
    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getId()).thenReturn(taskInstanceId);
    when(challengeService.findOne(any()))
        .thenReturn(challenge);

    TaskPair taskPairFromForm = new TaskPair("validClassName",
        "validName", "validName", "validDesc",
        "validDesc");
    mvc.perform(post("/createTaskPair")
        .with(csrf())
        .flashAttr("taskPair", taskPairFromForm))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/playChallenge/" + challenge.getId()));
  }

  @Test
  @WithMockUser
  public void cantGetCreateTaskpair() throws Exception {
    mvc.perform(get("/createChallenge"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @WithMockUser
  public void playOpenChallengeWithNonPlayerUserRedirectsToError() throws Exception {
    User user = Mockito.mock(User.class);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(user);

    when(challengeService.findOne(any())).thenReturn(challenge);

    when(challengeService.isParticipating(any(), any())).thenReturn(false);

    mvc.perform(get("/playChallenge/0")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error"));

    verify(challengeService, never())
        .save(any());
  }

  @Test
  @WithMockUser
  public void playOpenChallengeWithUnfinishedTaskInstanceOwnedByCurrentUserRedirectsToTask()
      throws Exception {
    Long userId = 723L;
    Long taskInstanceId = 992L;
    Long challengeId = 123L;

    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(userId);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getId()).thenReturn(992L);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challengeService.findOne(challengeId)).thenReturn(challenge);
    when(userService.getCurrentUser()).thenReturn(user);
    when(taskInstanceService.getUnfinishedInstanceInChallenge(challenge, user)).thenReturn(
                                                                                taskInstance);

    mvc.perform(get("/playChallenge/" + challengeId)
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/task/" + taskInstanceId));
  }

  @Test
  @WithMockUser
  public void playOpenChallengeWithUnfinishedTaskInstanceNotOwnedByCurrentUserRedirectsToUser()
      throws Exception {
    User user = Mockito.mock(User.class);
    User user2 = Mockito.mock(User.class);
    when(user.getId()).thenReturn(10L);
    when(user2.getId()).thenReturn(12L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user2);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(liveChallengeService.canParticipate(challenge, user)).thenReturn(true);
    when(taskInstanceService.getUnfinishedTaskInstance(any())).thenReturn(taskInstance);
    when(userService.getCurrentUser()).thenReturn(user);

    mvc.perform(get("/playChallenge/0")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));
    verify(liveChallengeService, never())
        .getTurnType(any(), any());
  }

  @Test
  @WithMockUser
  public void playOpenChallengeNotOnTheUsersTurnRedirectsToUser() throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(10L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(userService.getCurrentUser()).thenReturn(user);
    when(taskInstanceService.getUnfinishedTaskInstance(any())).thenReturn(null);
    when(liveChallengeService.canParticipate(challenge, user)).thenReturn(true);
    when(liveChallengeService.getTurnType(any(), any())).thenReturn(TurnType.NONE);

    mvc.perform(get("/playChallenge/0")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));

    verify(liveChallengeService, times(1))
        .getTurnType(any(), any());
  }

  @Test
  @WithMockUser
  public void playOpenChallengeOnImplementationTurnRedirectsToTask() throws Exception {
    Long testTaskId = 444L;
    Long implTaskId = 333L;
    Long taskInstanceId = 313L;
    Long newTaskInstanceId = 131L;
    Long challengeId = 123L;
    Long userId = 321L;

    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(userId);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getId()).thenReturn(taskInstanceId);
    TaskInstance newTaskInstance = Mockito.mock(TaskInstance.class);
    when(newTaskInstance.getId()).thenReturn(newTaskInstanceId);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getId()).thenReturn(challengeId);
    when(challenge.getIsOpen()).thenReturn(true);

    Task testTask = Mockito.mock(Task.class);
    when(testTask.getId()).thenReturn(testTaskId);
    when(testTask.getAuthor()).thenReturn(user);
    Task implTask = Mockito.mock(Task.class);
    when(implTask.getId()).thenReturn(implTaskId);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(liveChallengeService.canParticipate(challenge, user)).thenReturn(true);
    when(challenge.getType()).thenReturn(ChallengeType.PROJECT);
    when(taskService.getNumberOfTasks(challenge)).thenReturn(2);
    when(taskInstanceService.getUnfinishedTaskInstance(challenge)).thenReturn(null);
    when(liveChallengeService.getTurnType(challenge, user)).thenReturn(TurnType.IMPLEMENTATION);
    when(liveChallengeService
        .getTopmostImplementationTask(challenge)).thenReturn(implTask);
    when(liveChallengeService.getTopmostTestTask(challenge)).thenReturn(testTask);
    when(taskInstanceService.getByTaskAndUser(testTask, user)).thenReturn(taskInstance);
    when(userService.getCurrentUser()).thenReturn(user);
    when(taskInstanceService.createEmpty(user, implTask)).thenReturn(newTaskInstance);
    when(gameplayService.newTaskInstance(implTask, taskInstance, user)).thenReturn(newTaskInstance);
    when(newTaskInstance.getChallenge()).thenReturn(challenge);

    mvc.perform(get("/playChallenge/0")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/task/" + newTaskInstanceId));
  }

  @Test
  @WithMockUser
  public void playLiveOnTestTurnRedirectsToNewTaskPair() throws Exception {
    Long taskId = 0L;
    Long challengeId = 877L;

    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getId()).thenReturn(challengeId);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    Task task = Mockito.mock(Task.class);
    when(task.toString()).thenReturn("");
    when(task.getId()).thenReturn(taskId);
    when(task.getAuthor()).thenReturn(user);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(liveChallengeService.canParticipate(challenge, user)).thenReturn(true);
    when(taskInstanceService.getUnfinishedTaskInstance(any())).thenReturn(null);
    when(liveChallengeService.getTurnType(any(), any())).thenReturn(TurnType.TEST);
    when(userService.getCurrentUser()).thenReturn(user);

    when(liveChallengeService.getTopmostImplementationTask(any())).thenReturn(task);
    when(liveChallengeService.getTopmostTestTask(any())).thenReturn(task);
    when(taskInstanceService.getByTaskAndUser(any(), any())).thenReturn(taskInstance);

    mvc.perform(get("/playChallenge/" + challengeId)
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/newtaskpair/" + challengeId));
  }

  @Test
  @WithMockUser
  public void closingChallengeNotOwnedByCurrentUserRedirectsToError() throws Exception {
    when(challengeService.isParticipating(any(), any())).thenReturn(false);

    mvc.perform(post("/closeChallenge/0")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error"));

    verify(challengeService, never())
        .save(any());
  }

  @Test
  @WithMockUser
  public void closingChallengeOwnedByCurrentUserRedirectsToUser() throws Exception {
    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getId()).thenReturn(20L);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(challengeService.isParticipating(any(), any())).thenReturn(true);

    mvc.perform(post("/closeChallenge/0")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));
  }

}
