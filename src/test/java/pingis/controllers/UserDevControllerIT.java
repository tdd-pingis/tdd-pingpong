package pingis.controllers;

import java.security.Principal;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.ui.Model;

/**
 *
 * @author villburn
 */
public class UserDevControllerIT {
    
    public UserDevControllerIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testLogin() {
        System.out.println("login");
        Model model = null;
        UserDevController instance = new UserDevController();
        String expResult = "";
        String result = instance.login(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUser() {
        System.out.println("user");
        Model model = null;
        Principal principal = null;
        UserDevController instance = new UserDevController();
        String expResult = "";
        String result = instance.user(model, principal);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testAdmin() {
        System.out.println("admin");
        Model model = null;
        UserDevController instance = new UserDevController();
        String expResult = "";
        String result = instance.admin(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
