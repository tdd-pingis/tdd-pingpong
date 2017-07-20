package entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskTest {

    Task normalTask, protectedTask;

    @Before
    public void setUp() {
        normalTask = new Task("generate toString()");
        protectedTask = new Task();
    }

    @Test
    public void testTaskCode() {
        assertEquals("generate toString()", normalTask.getCode());
    }

    @Test
    public void testTaskImplementations() {
        assertEquals(0, normalTask.getImplementations().size());
    }

    @Test
    public void testTaskRating() {
        assertEquals(0, normalTask.getRating(),0.001);
    }

}
