package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskInstanceTest {

    private static final int TMC_USER_LEVEL = 100;
    
    TaskInstance userImplementation, protectedImplementation;
    Task testTask;
    User authorUser;
    
    @Before
    public void setUp() {
        authorUser = new User(1, "ModelUser", TMC_USER_LEVEL);
        testTask = new Task(0,
                TaskType.IMPLEMENTATION,
                            authorUser,
                            "Test Addition",
                            "Test addition with two integers.",
                            "Some assertive testcode here.",
                            1,
                            0);
        
        userImplementation = new TaskInstance(
                            authorUser, 
                            "return true;", 
                            testTask);
        
        protectedImplementation = new TaskInstance();
    }

    @Test
    public void testImplementationCode() {
        assertEquals("return true;", userImplementation.getCode());
    }

}
