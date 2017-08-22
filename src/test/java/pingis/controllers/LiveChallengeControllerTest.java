package pingis.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
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
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import pingis.services.ChallengeService;
import pingis.services.GameplayService;
import pingis.services.GameplayService.TurnType;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;
import pingis.services.UserService;

/**
 *
 * @author authority
 */
@RunWith(SpringRunner.class)
@WebMvcTest(LiveChallengeController.class)
public class LiveChallengeControllerTest {

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

  @Test
  @WithMockUser
  public void newChallengeReturnsOk() throws Exception {
    mvc.perform(get("/newchallenge"))
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
  public void creatingChallengeRedirectsToPlayTurn() throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);
    when(userService.getCurrentUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challengeService.save(any())).thenAnswer(i -> i.getArguments()[0]);
    when(challenge.toString()).thenReturn("");
    when(challenge.getId()).thenReturn(1L);

    mvc.perform(post("/createChallenge")
            .with(csrf())
            .param("challengeName", "name")
            .param("challengeDesc", "desc")
            .param("challengeType", "PROJECT")
            .param("realm", "realm"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/playTurn/*"));
  }

  @Test
  @WithMockUser
  public void newTaskpairReturnsNewTaskPairView() throws Exception {
    mvc.perform(get("/newtaskpair")
            .flashAttr("challengeId", 0L)
            .flashAttr("challenge", new Challenge("n", new User(), "d")))
            .andExpect(status().isOk())
            .andExpect(view().name("newtaskpair"));
  }

  @Test
  @WithMockUser
  public void createTaskPairRedirectsToPlayTurn() throws Exception {
    Challenge challenge = Mockito.mock(Challenge.class);
    when(challengeService.findOne(any()))
            .thenReturn(challenge);

    Task task = Mockito.mock(Task.class);
    when(gameplayService.generateTaskPairAndTaskInstance(
            any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(task);

    mvc.perform(post("/createTaskPair")
            .with(csrf())
            .param("testTaskName", "name")
            .param("implementationTaskname", "name")
            .param("testTaskDesc", "desc")
            .param("implementationTaskDesc", "desc")
            .param("testCodeStub", "stub")
            .param("implementationCodeStub", "stub")
            .param("challengeId", "0"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/playTurn/*"));
  }

  @Test
  @WithMockUser
  public void cantGetCreateTaskpair() throws Exception {
    mvc.perform(get("/createChallenge"))
            .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @WithMockUser
  public void playTurnWithClosedChallengeRedirectsToError() throws Exception {
    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(false);

    when(challengeService.findOne(any())).thenReturn(challenge);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser
  public void playTurnWithNonPlayerUserRedirectsToError() throws Exception {
    User user = Mockito.mock(User.class);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(user);

    when(challengeService.findOne(any())).thenReturn(challenge);

    when(gameplayService.isParticipating(any())).thenReturn(false);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser
  public void playTurnWithUnfinishedTaskInstanceOwnedByCurrentUserRedirectsToTask()
          throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    Task task = Mockito.mock(Task.class);
    when(task.toString()).thenReturn("");
    when(task.getId()).thenReturn(0L);
    when(task.getAuthor()).thenReturn(user);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(gameplayService.isParticipating(any())).thenReturn(false);

    when(challengeService.getUnfinishedTaskInstance(any())).thenReturn(taskInstance);
    when(userService.getCurrentUser()).thenReturn(user);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/task/0"));
  }

  @Test
  @WithMockUser
  public void playTurnWithUnfinishedTaskInstanceNotOwnedByCurrentUserRedirectsToUser()
          throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(gameplayService.isParticipating(any())).thenReturn(false);

    when(challengeService.getUnfinishedTaskInstance(any())).thenReturn(taskInstance);
    when(userService.getCurrentUser()).thenReturn(null);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user"));
  }

  @Test
  @WithMockUser
  public void playTurnNotOnTheUsersTurnRedirectsToUser() throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(gameplayService.isParticipating(any())).thenReturn(false);
    when(challengeService.getUnfinishedTaskInstance(any())).thenReturn(taskInstance);
    when(userService.getCurrentUser()).thenReturn(null);

    when(gameplayService.getTurnType(any())).thenReturn(TurnType.NONE);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user"));
  }

  @Test
  @WithMockUser
  public void playTurnOnImplementationTurnRedirectsToNewTaskInstance() throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    Task task = Mockito.mock(Task.class);
    when(task.toString()).thenReturn("");
    when(task.getId()).thenReturn(0L);
    when(task.getAuthor()).thenReturn(user);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(gameplayService.isParticipating(any())).thenReturn(false);
    when(challengeService.getUnfinishedTaskInstance(any())).thenReturn(null);
    when(gameplayService.getTurnType(any())).thenReturn(TurnType.IMPLEMENTATION);

    when(gameplayService.getTopmostImplementationTask(any())).thenReturn(task);
    when(gameplayService.getTopmostTestTask(any())).thenReturn(task);
    when(taskInstanceService.getByTaskAndUser(any(), any())).thenReturn(taskInstance);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/newTaskInstance*"));
  }

  @Test
  @WithMockUser
  public void playTurnOnTestTurnRedirectsToNewTaskInstance() throws Exception {
    User user = Mockito.mock(User.class);
    when(user.getId()).thenReturn(0L);

    TaskInstance taskInstance = Mockito.mock(TaskInstance.class);
    when(taskInstance.getUser()).thenReturn(user);

    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getIsOpen()).thenReturn(true);
    when(challenge.getSecondPlayer()).thenReturn(null);

    Task task = Mockito.mock(Task.class);
    when(task.toString()).thenReturn("");
    when(task.getId()).thenReturn(0L);
    when(task.getAuthor()).thenReturn(user);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(gameplayService.isParticipating(any())).thenReturn(false);
    when(challengeService.getUnfinishedTaskInstance(any())).thenReturn(null);
    when(gameplayService.getTurnType(any())).thenReturn(TurnType.TEST);

    when(gameplayService.getTopmostImplementationTask(any())).thenReturn(task);
    when(gameplayService.getTopmostTestTask(any())).thenReturn(task);
    when(taskInstanceService.getByTaskAndUser(any(), any())).thenReturn(taskInstance);

    mvc.perform(get("/playTurn/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/newtaskpair"));
  }

  @Test
  @WithMockUser
  public void closingChallengeNotOwnedByCurrentUserRedirectsToError() throws Exception {
    when(gameplayService.isParticipating(any())).thenReturn(false);

    mvc.perform(post("/closeChallenge/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser
  public void closingChallengeOwnedByCurrentUserRedirectsToUser() throws Exception {
    Challenge challenge = Mockito.mock(Challenge.class);
    when(challenge.getId()).thenReturn(0L);

    when(challengeService.findOne(any())).thenReturn(challenge);
    when(gameplayService.isParticipating(any())).thenReturn(true);

    mvc.perform(post("/closeChallenge/0")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user"));
  }

}
