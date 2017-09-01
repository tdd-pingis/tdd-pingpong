package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Challenge;
import pingis.entities.User;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.UserService;
import pingis.services.logic.DashboardService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DashboardService.class})
public class DashboardServiceTest {

  @Autowired
  private DashboardService dashboardService;

  @MockBean
  ChallengeService challengeServiceMock;

  @MockBean
  UserService userServiceMock;

  private Challenge testChallenge;
  private User testUser;

  @Before
  public void setUp() {
    testUser = new User(1, "pekka", 1);
    testUser.setId(123);
    testChallenge = new Challenge("testihaaste", testUser, "testihaaste");
    testChallenge.setOpen(true);
  }

  @Test
  public void testGetParticipatingLiveChallenge() {
    List<Challenge> challenges = new ArrayList<>();
    challenges.add(testChallenge);
    when(challengeServiceMock.findAll()).thenReturn(challenges);
    when(challengeServiceMock.isParticipating(any(), any())).thenReturn(true);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    assertEquals(testChallenge.getDesc(), dashboardService
        .getParticipatingLiveChallenge()
        .getDesc());
    verify(challengeServiceMock).findAll();
    verify(challengeServiceMock).isParticipating(any(), any());
    verify(userServiceMock).getCurrentUser();
    verifyNoMoreInteractions(challengeServiceMock);
    verifyNoMoreInteractions(userServiceMock);
  }
}
