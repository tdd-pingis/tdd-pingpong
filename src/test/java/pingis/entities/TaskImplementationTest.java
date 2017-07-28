package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskImplementationTest {

    TaskImplementation userImplementation, protectedImplementation;

    @Before
    public void setUp() {
        userImplementation = new TaskImplementation("return true;", ImplementationType.IMPLEMENTATION);
        protectedImplementation = new TaskImplementation();
    }

    @Test
    public void testImplementationCode() {
        assertEquals("return true;", userImplementation.getCode());
    }

}
