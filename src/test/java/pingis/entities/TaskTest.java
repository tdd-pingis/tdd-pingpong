package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskTest {

    Task normalTask, protectedTask;

    @Before
    public void setUp() {
        normalTask = new Task(1,
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
    public void testTaskImplementations() {
        assertEquals(0, normalTask.getImplementations().size());
    }

    @Test
    public void testTaskRating() {
        final double RATING = 0.001;
        assertEquals(0, normalTask.getRating(), RATING);
    }
    
}
