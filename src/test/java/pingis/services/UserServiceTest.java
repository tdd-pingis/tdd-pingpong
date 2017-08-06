/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.Application;
import pingis.entities.OAuthUser;
import pingis.entities.User;
import pingis.entities.Task;
import pingis.repositories.UserRepository;
import static pingis.services.DataImporter.UserType.TEST_USER;
import static pingis.services.DataImporter.UserType.TMC_MODEL_USER;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class UserServiceTest {
    
    private final int TEST_USER_ID = 1;
    private final String TEST_USER_NAME = "Test_user1";
    private final int TEST_USER_LEVEL = 3;
    
    private final int TEST_USER2_ID = 2;
    private final String TEST_USER2_NAME = "Test_user2";
    private final int TEST_USER2_LEVEL = 5;
    
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
        assertThat(oneUser.getLevel()).isEqualTo(testUser.getLevel());
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

        boolean deleted = userService.contains(userCaptor.getValue().getId());
        assertFalse(deleted);
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
        when(userRepositoryMock.exists(testUser.getId())).thenReturn(true);
        userService.save(testUser);
        
        boolean contains = userService.contains(testUser.getId());
        verify(userRepositoryMock).exists(testUser.getId());
        assertThat(contains).isTrue();
    }

    @Test
    public void testContainsWithTwoUsers() {
        when(userRepositoryMock.exists(testUser.getId())).thenReturn(true);
        when(userRepositoryMock.exists(testUser2.getId())).thenReturn(true);
        
        userService.save(testUser);
        userService.save(testUser2);
        
        boolean contains = userService.contains(testUser.getId());
        boolean contains2 = userService.contains(testUser2.getId());
        
        verify(userRepositoryMock).exists(testUser.getId());
        verify(userRepositoryMock).exists(testUser2.getId());
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
        OAuthUser user = generateOauthTestUser();
        userService.initializeUser(user);
        verify(userRepositoryMock).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getName()).isEqualTo(user.getName());
        assertThat(userCaptor.getValue().getId()).isEqualTo(Long.parseLong(user.getId()));
        assertThat(userCaptor.getValue().getLevel()).isEqualTo(1);
        assertThat(userCaptor.getValue().isAdministrator()).isEqualTo(user.isAdministrator());
    }
    
    @Test
    public void testVerifyUserInitializationWithRandomUser() {
        userService.save(new User("TEST1"));
        userService.save(new User("TEST2"));
        
        verify(userRepositoryMock, times(2)).save(userCaptor.capture());
        when(userRepositoryMock.exists(userCaptor.getAllValues().get(0).getId())).thenReturn(Boolean.FALSE);
        when(userRepositoryMock.exists(userCaptor.getAllValues().get(1).getId())).thenReturn(Boolean.FALSE);
        
        try {
            userService.verifyUserInitialization();
            assertEquals(this, this);
            assertTrue("VerifyUserInitialization should throw IllegalStateException if user-ids are "
                        + "different than the, now it didnt", false);
        } catch (IllegalStateException ie) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testVerifyUserInitializationWithRightIDs() {
        userService.save(new User(TMC_MODEL_USER.getId(),TMC_MODEL_USER.getLogin(), 
                                      Task.LEVEL_MAX_VALUE, TMC_MODEL_USER.isAdmin()));
        userService.save(new User(TEST_USER.getId(),TEST_USER.getLogin(), 
                                      TEST_USER2_LEVEL ,TEST_USER.isAdmin()));
        
        verify(userRepositoryMock, times(2)).save(userCaptor.capture());
        when(userRepositoryMock.exists(TEST_USER.getId())).thenReturn(Boolean.TRUE);
        when(userRepositoryMock.exists(TMC_MODEL_USER.getId())).thenReturn(Boolean.TRUE);
        
        try {
            userService.verifyUserInitialization();
            assertTrue(true);
        } catch (IllegalStateException ie) {
            assertFalse(ie.getMessage(), true);
        }
    }
    
    @Test
    public void testDoesNotAuthenticateUnknownOAuthUser() {
        ArgumentCaptor<Long> capturedId = ArgumentCaptor.forClass(Long.class);
        OAuthUser user = new OAuthUser();
        String randomUserId = Long.toString(new Random(Long.MAX_VALUE).nextLong());
        
        user.setAdministrator(true);
        user.setName(TEST_USER2_NAME);
        user.setId(randomUserId);
        
        when(userRepositoryMock.exists(Long.parseLong(randomUserId))).thenReturn(false);
        boolean authenticated = userService.authenticateOAuthUser(user);
        verify(userRepositoryMock).exists(capturedId.capture());
        
        assertThat(authenticated).isFalse();
        assertThat(capturedId.getValue()).isEqualTo((Long.parseLong(randomUserId)));
    }
    
    @Test
    public void testAuthenticateWithKnownOAuthUser() {
        ArgumentCaptor<Long> capturedId = ArgumentCaptor.forClass(Long.class);
        OAuthUser user = generateOauthTestUser();
        
        userService.save(testUser);
        verify(userRepositoryMock).save(userCaptor.capture());
        
        when(userRepositoryMock.exists((long) TEST_USER_ID)).thenReturn(true);
        boolean authenticated = userService.authenticateOAuthUser(user);
        
        verify(userRepositoryMock).exists(capturedId.capture());
        assertThat(authenticated).isTrue();
        assertThat(capturedId.getValue()).isEqualTo((long) TEST_USER_ID);
    }

    private OAuthUser generateOauthTestUser() {
        OAuthUser user = new OAuthUser();
        user.setAdministrator(true);
        user.setName(TEST_USER_NAME);
        user.setId(Long.toString(TEST_USER_ID));
        return user;
    }
    
    @Test
    public void testAuthenticateKnownUser() {
        userService.save(testUser);
        verify(userRepositoryMock).save(userCaptor.capture());
        
        when(userRepositoryMock.findByName(testUser.getName())).thenReturn(userCaptor.getValue());
        boolean authenticated = userService.authenticateUser(testUser.getName());
        assertThat(authenticated).isTrue();
    }
    
    @Test
    public void testDoesNotAuthenticateUnknownUser() {
        userService.save(testUser);
        verify(userRepositoryMock).save(userCaptor.capture());
        
        when(userRepositoryMock.findByName(testUser2.getName())).thenReturn(null);
        boolean authenticated = userService.authenticateUser(testUser2.getName());
        assertThat(authenticated).isFalse();
    }
    
    
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
    }
    
}