package pingis.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.TmcUserDto;
import pingis.entities.User;
import pingis.repositories.UserRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserService.class})
public class UserServiceTest {

  private static final int TEST_USER_ID = 1;
  private static final String TEST_USER_NAME = "Test_user1";
  private static final int TEST_USER_LEVEL = 3;

  private static final int TEST_USER2_ID = 2;
  private static final String TEST_USER2_NAME = "Test_user2";
  private static final int TEST_USER2_LEVEL = 5;

  @Autowired
  UserService userService;

  @MockBean
  private UserRepository userRepositoryMock;

  private User testUser;
  private User testUser2;
  private ArgumentCaptor<User> userCaptor;

  @Before
  public void setUp() {
    testUser = new User(TEST_USER_ID, TEST_USER_NAME, TEST_USER_LEVEL);
    testUser2 = new User(TEST_USER2_ID, TEST_USER2_NAME, TEST_USER2_LEVEL);
    userCaptor = ArgumentCaptor.forClass(User.class);
  }

  @Test
  public void testSaveOneUser() {
    userService.save(testUser);
    verify(userRepositoryMock, times(1)).save(userCaptor.capture());
    assertThat(userCaptor.getValue()).isEqualTo(testUser);
  }

  @Test
  public void testSimpleSaveAndFindOneUserFind() {
    userService.save(testUser);
    userService.findByName(testUser.getName());
    verify(userRepositoryMock, times(1)).save(userCaptor.capture());
    verify(userRepositoryMock, times(1)).findByName(userCaptor.getValue().getName());
    verifyNoMoreInteractions(userRepositoryMock);

    User oneUser = userCaptor.getValue();

    assertThat(oneUser.getName()).isEqualTo(testUser.getName());
    assertThat(oneUser.getId()).isEqualTo(testUser.getId());
    assertThat(oneUser.getPoints()).isEqualTo(testUser.getPoints());
  }

  @Test
  public void testSimpleFindAllUsers() {
    userService.save(testUser);

    List<User> testUsers = new ArrayList<User>();
    testUsers.add(testUser);

    when(userRepositoryMock.findAll()).thenReturn(testUsers);
    List<User> found = userService.findAll();

    verify(userRepositoryMock, times(1)).findAll();
    assertEquals(found.size(), testUsers.size());
  }

  @Test
  public void testfindAllUsersWithTwoUsers() {
    userService.save(testUser);
    userService.save(testUser2);

    List<User> testUsers = new ArrayList<User>();
    testUsers.add(testUser);
    testUsers.add(testUser2);

    when(userRepositoryMock.findAll()).thenReturn(testUsers);
    List<User> found = userService.findAll();

    verify(userRepositoryMock, times(1)).findAll();
    assertEquals(found.size(), testUsers.size());
  }

  @Test
  public void testSimpleFindAlltWithMultipleInputUsers() {
    userService.save(testUser);
    userService.save(testUser2);

    List<User> testUsers = new ArrayList<User>();
    testUsers.add(testUser);
    testUsers.add(testUser2);

    when(userRepositoryMock.findAll()).thenReturn(testUsers);
    List<User> found = userService.findAll();

    verify(userRepositoryMock, times(1)).findAll();
    assertEquals(found.size(), testUsers.size());

    assertThat(found.get(0).getName()).isEqualTo(testUser.getName());
    assertThat(found.get(1).getName()).isEqualTo(testUser2.getName());
  }

  @Test
  public void testSimpleDeleteOneUser() {
    userService.save(testUser);
    verify(userRepositoryMock, times(1)).save(userCaptor.capture());

    userService.delete(userCaptor.getValue());
    verify(userRepositoryMock).delete(userCaptor.getValue());

    boolean userExists = userService.contains(userCaptor.getValue().getId());
    assertFalse(userExists);
  }

  @Test
  public void testSimpleDeleteMultipleUsers() {

    userService.save(testUser);
    userService.save(testUser2);
    verify(userRepositoryMock, times(2)).save(userCaptor.capture());

    List<User> capturedUsers = userCaptor.getAllValues();

    int userIndex = 0;

    userService.delete(testUser);
    verify(userRepositoryMock, times(1)).delete(capturedUsers.get(userIndex));

    userService.delete(testUser2);
    verify(userRepositoryMock, times(1)).delete(capturedUsers.get(++userIndex));
    verifyNoMoreInteractions(userRepositoryMock);
  }

  @Test
  public void testContains() {
    when(userRepositoryMock.existsById(testUser.getId())).thenReturn(true);
    userService.save(testUser);

    boolean contains = userService.contains(testUser.getId());
    verify(userRepositoryMock).existsById(testUser.getId());
    assertThat(contains).isTrue();
  }

  @Test
  public void testContainsWithTwoUsers() {
    when(userRepositoryMock.existsById(testUser.getId())).thenReturn(true);
    when(userRepositoryMock.existsById(testUser2.getId())).thenReturn(true);

    userService.save(testUser);
    userService.save(testUser2);

    boolean contains = userService.contains(testUser.getId());
    boolean contains2 = userService.contains(testUser2.getId());

    verify(userRepositoryMock).existsById(testUser.getId());
    verify(userRepositoryMock).existsById(testUser2.getId());
    assertThat(contains && contains2).isTrue();
  }

  @Test
  public void testDeleteMultipleWithTwo() {
    List<User> delUsers = Arrays.asList(testUser, testUser2);

    userService.save(testUser);
    userService.save(testUser2);

    userService.deleteMultiple(delUsers);
    verify(userRepositoryMock).delete(testUser);
    verify(userRepositoryMock).delete(testUser2);
  }

  @Test
  public void testInitializeUserWithOauthUser() {
    TmcUserDto user = generateOauthTestUser();
    userService.initializeUser(user);
    verify(userRepositoryMock).save(userCaptor.capture());
    assertThat(userCaptor.getValue().getName()).isEqualTo(user.getName());
    assertThat(userCaptor.getValue().getId()).isEqualTo(Long.parseLong(user.getId()));
    assertThat(userCaptor.getValue().getPoints()).isEqualTo(1);
    assertThat(userCaptor.getValue().isAdministrator()).isEqualTo(user.isAdministrator());
  }

  @Test
  public void testHandleUnknownOAuthUser() {
    TmcUserDto oauthUser1 = new TmcUserDto();
    Long randomUserId = new Random(Long.MAX_VALUE).nextLong();
    String randomUserIdString = Long.toString(randomUserId);

    oauthUser1.setAdministrator(true);
    oauthUser1.setName(TEST_USER2_NAME);
    oauthUser1.setId(randomUserIdString);

    when(userRepositoryMock.findById(Long.parseLong(randomUserIdString)))
            .thenReturn(Optional.empty());
    when(userRepositoryMock.save(userCaptor.capture())).thenReturn(testUser2);
    User newUser = userService.handleOAuthUserAuthentication(oauthUser1);

    ArgumentCaptor<Long> capturedId = ArgumentCaptor.forClass(Long.class);

    verify(userRepositoryMock).findById(capturedId.capture());
    verify(userRepositoryMock, times(1)).save(userCaptor.capture());

    assertThat(newUser).isNotNull();
    assertThat(newUser.getName()).isEqualTo(TEST_USER2_NAME);
    assertThat(userCaptor.getValue().getId()).isEqualTo((Long.parseLong(randomUserIdString)));
  }

  @Test
  public void testHandleKnownOAuthUser() {
    userService.save(testUser);
    verify(userRepositoryMock).save(userCaptor.capture());

    when(userRepositoryMock.findById((long) TEST_USER_ID)).thenReturn(Optional.of(testUser));

    TmcUserDto user = generateOauthTestUser();
    User newUser = userService.handleOAuthUserAuthentication(user);

    ArgumentCaptor<Long> capturedId = ArgumentCaptor.forClass(Long.class);

    verify(userRepositoryMock).findById(capturedId.capture());
    verify(userRepositoryMock).save(testUser);
    assertThat(newUser).isEqualTo(testUser);
    assertThat(capturedId.getValue()).isEqualTo((long) TEST_USER_ID);
  }

  private TmcUserDto generateOauthTestUser() {
    TmcUserDto user = new TmcUserDto();
    user.setAdministrator(true);
    user.setName(TEST_USER_NAME);
    user.setId(Long.toString(TEST_USER_ID));

    return user;
  }

  @Test
  public void testHandleKnownUser() {
    userService.save(testUser);
    verify(userRepositoryMock).save(userCaptor.capture());

    when(userRepositoryMock.findByName(testUser.getName())).thenReturn(userCaptor.getValue());
    User newUser = userService.handleUserAuthenticationByName(TEST_USER_NAME);
    assertThat(newUser).isEqualTo(testUser);
  }

  @Test
  public void testHandleUnknownUser() {
    when(userRepositoryMock.findByName(testUser2.getName())).thenReturn(null);
    when(userRepositoryMock.save(testUser2)).thenReturn(testUser2);
    User newUser = userService.handleUserAuthenticationByName(testUser2.getName());

    assertThat(newUser).isNotNull();
    assertThat(newUser.getName()).isEqualTo(testUser2.getName());
    assertThat(newUser.getId()).isExactlyInstanceOf(Long.class);
  }

}
