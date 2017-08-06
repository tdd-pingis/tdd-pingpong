package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskImplementationTest {

    private static final int TMC_USER_LEVEL = 100;
    
    TaskImplementation userImplementation, protectedImplementation;
    Task testTask;
    User authorUser;
    
    @Before
    public void setUp() {
        authorUser = new User(1, "ModelUser", TMC_USER_LEVEL);
        testTask = new Task(0,
                ImplementationType.IMPLEMENTATION,
                            authorUser,
                            "Test Addition",
                            "Test addition with two integers.",
                            "Some assertive testcode here.",
                            1,
                            0);
        
        userImplementation = new TaskImplementation(
                            authorUser, 
                            "return true;", 
                            testTask);
        
        protectedImplementation = new TaskImplementation();
    }

    @Test
    public void testImplementationCode() {
        assertEquals("return true;", userImplementation.getCode());
    }

}
