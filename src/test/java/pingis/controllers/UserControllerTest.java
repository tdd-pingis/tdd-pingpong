/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.controllers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pingis.Application;
import pingis.config.SecurityConfig;
import pingis.config.SecurityDevConfig;
import pingis.entities.User;
import pingis.repositories.UserRepository;
import pingis.services.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class, UserController.class, SecurityConfig.class})
@WebAppConfiguration
@WebMvcTest(IndexController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;
    
    @MockBean
    UserRepository userRepositoryMock;

    private ArgumentCaptor<User> userCaptor;

    public UserControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /*
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }*/
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSimpleUserStatusOk() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
        verify(userRepositoryMock).save(userCaptor.capture());
        assertTrue("Captured: " + userCaptor.getValue().toString(), false);
    }
    
    @Test
    public void testLogin() throws Exception {
        getLoginOk();
        assertTrue(true);
    }
    
    @Test
    public void testAdmin() throws Exception {
        mockMvc.perform(post("/user"))
                .andExpect(status().isOk());
        
        assertTrue(true);
    }

    private void getLoginOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
        verifyNoMoreInteractions(userRepositoryMock);
    }

}
