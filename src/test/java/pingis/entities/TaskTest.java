package pingis.entities;


import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskTest {

    Task normalTask, protectedTask;

    @Before
    public void setUp() {
        normalTask = new Task(1, TaskType.TEST,
                new User(new Random().nextLong(), "Test_user", 1),
                "tostring", 
                "generate toString()",
                "Some amazing testCode here",
                1, 0);
        protectedTask = new Task();
    }

    @Test
    public void testTaskCode() {
        assertEquals("generate toString()", normalTask.getDesc());
    }
    
    @Test
    public void testTaskInstances() {
        assertEquals(0, normalTask.getTaskInstances().size());
    }

    @Test
    public void testTaskRating() {
        final double RATING = 0.001;
        assertEquals(0, normalTask.getRating(), RATING);
    }
    
}
