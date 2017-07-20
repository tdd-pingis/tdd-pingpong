package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StateTest {

    State protectedState;
    @Before
    public void setUp() {
        protectedState = new State("done");
    }

    @Test
    public void testStateStatus() {
        assertEquals("done", protectedState.getStatus());
    }

}
