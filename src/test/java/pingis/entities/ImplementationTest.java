package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImplementationTest {

    Implementation userImplementation, protectedImplementation;

    @Before
    public void setUp() {
        userImplementation = new Implementation("return true;");
        protectedImplementation = new Implementation();
    }

    @Test
    public void testImplementationCode() {
        assertEquals("return true;", userImplementation.getCode());
    }

}
