package pingis.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.Application;
import pingis.entities.Challenge;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ChallengeService.class})
public class ChallengeServiceTest {

  @Autowired
  private ChallengeService challengeService;

  @MockBean
  private ChallengeRepository challengeRepositoryMock;

  private User testUser;
  private Challenge testChallenge;
  private ArgumentCaptor<Challenge> challengeCaptor;

  @Before
  public void setUp() {
    testUser = new User(1, "Matti", 1);
    testChallenge = new Challenge("Calculator", testUser, "SimpleCalculatorDesc");
    challengeCaptor = ArgumentCaptor.forClass(Challenge.class);
  }


  @Test
  public void simpleSaveAndFindOneChallengeTest() {
    challengeService.save(testChallenge);
    challengeService.findByName(testChallenge.getName());

    verify(challengeRepositoryMock, times(1)).save(challengeCaptor.capture());
    verify(challengeRepositoryMock, times(1)).findByName(challengeCaptor.getValue().getName());
    verifyNoMoreInteractions(challengeRepositoryMock);

    Challenge oneChallenge = challengeCaptor.getValue();

    assertEquals(oneChallenge.getDesc(), testChallenge.getDescription());
  }

  @Test
  public void simpleFindAllChallengesTest() {
    challengeService.save(testChallenge);

    List<Challenge> testChallenges = new ArrayList<Challenge>();
    testChallenges.add(testChallenge);

    when(challengeRepositoryMock.findAll()).thenReturn(testChallenges);
    List<Challenge> found = challengeService.findAll();

    verify(challengeRepositoryMock, times(1)).findAll();
    assertEquals(found.size(), testChallenges.size());
  }

  @Test
  public void simpleDeleteOneChallengeTest() {
    challengeService.save(testChallenge);

    verify(challengeRepositoryMock, times(1)).save(challengeCaptor.capture());

    challengeService.delete(challengeCaptor.getValue().getId());

    verify(challengeRepositoryMock, times(1)).delete(challengeCaptor.getValue().getId());
    boolean deleted = challengeService.contains(challengeCaptor.getValue().getId());
    verify(challengeRepositoryMock, times(1)).exists(challengeCaptor.getValue().getId());
    assertFalse(deleted);
  }

}
