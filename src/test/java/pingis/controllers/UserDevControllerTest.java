package pingis.controllers;

import javax.servlet.Filter;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pingis.config.SecurityDevConfig;
import pingis.entities.User;
import pingis.services.UserService;
import pingis.services.DataImporter.UserType;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserDevController.class)
@ContextConfiguration(classes = {UserDevController.class, SecurityDevConfig.class, UserService.class})
@WebAppConfiguration
public class UserDevControllerTest {
    
    private static final int TEST_USER_LEVEL = 200;
    private static final User testUser = new User(UserType.TEST_USER.getId(),
                                                  UserType.TEST_USER.name(),
                                                  TEST_USER_LEVEL);
    @Autowired
    WebApplicationContext context;
    
    @Autowired 
    private Filter springSecurityFilterChain; 
    
    private MockMvc mvc;
    
    @MockBean
    UserService userServiceMock;
    
    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .build();
    }
    
    /*
    @Test
    public void testCannotAccessUserWithoutLogin() throws Exception {
        performSimpleGetRequestAndFindContent("/user", "user", status().is3xxRedirection());
        Mockito.verifyNoMoreInteractions(userServiceMock);
    }
    
    @Test
    public void testGivenLoginCanAccessUserLogin() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user");

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        performSimpleGetRequestAndFindContent("/user", "user", status().isOk());
        verify(userServiceMock).handleUserAuthenticationByName(null);
    }
    */
    
    @Test
    @WithMockCustomUser
    public void testGivenLoginUserGetsAuthenticated() throws Exception {
        // Expected outcomes
        String expectedUsername = "user";
        String expectedViewName = "user";
        String testUrl = "/user";
        
        when(userServiceMock.handleUserAuthenticationByName(Mockito.anyString())).thenReturn(testUser);

        MvcResult result = mvc.perform(get(testUrl))
                .andExpect(view().name(expectedViewName))
                .andExpect(content().string(containsString(expectedUsername)))
                .andReturn();

        verify(userServiceMock).handleUserAuthenticationByName("user");
    }
    /*
    private void performSimpleGetRequestAndFindContent(String uri,
                                                       String viewName,
                                                       ResultMatcher expected) throws Exception {
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name(viewName))
                .andExpect(expected);
    }*/
    
}
