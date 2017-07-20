package pingis.entities;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    User normalUser, protectedUser;

    @Before
    public void setUp() {
        normalUser = new User("Matti", 1);
        protectedUser = new User();
    }

    @Test
    public void testUserName() {
        assertEquals("Matti", normalUser.getName());
    }

    @Test
    public void testUserLevel() {
        final double LEVEL = 0.001;
        assertEquals(1, normalUser.getLevel(), LEVEL);
    }

    @Test
    public void getTestStates() {
        assertEquals(0, normalUser.getStates().size());
    }

}
