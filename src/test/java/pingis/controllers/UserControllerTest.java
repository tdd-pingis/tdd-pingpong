package pingis.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pingis.config.SecurityDevConfig;
import pingis.entities.User;
import pingis.services.UserService;
import pingis.services.DataImporter.UserType;


@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = {UserDevController.class, SecurityDevConfig.class})
@WebAppConfiguration
public class UserControllerTest {
    
    private static final int TEST_USER_LEVEL = 200;
    private static final User testUser = new User(UserType.TEST_USER.getId(),
                                                  UserType.TEST_USER.name(),
                                                  TEST_USER_LEVEL);
    @Autowired
    WebApplicationContext context;
    
    private MockMvc mvc;
    
    @MockBean
    UserService userServiceMock;
    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
    
    @Test
    public void testCannotAccessUserWithoutLogin() throws Exception {
        performSimpleGetRequestAndFindResult("/user", "user", status().is3xxRedirection());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }
    
    /*
    @Test
    public void testGivenLoginCanAccessUserLogin() throws Exception {
        performSimpleGetRequestAndFindResult("/user", "user", status().isOk());
        verify(userServiceMock).handleOAuthUserAuthentication(null);
    }
    */
    
    private void performSimpleGetRequestAndFindResult(String uri,
                                                       String viewName,
                                                       ResultMatcher expected) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name(viewName))
                .andExpect(expected);
    }
    
}
