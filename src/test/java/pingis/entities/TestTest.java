package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestTest {

    pingis.entities.Test normalTest, protectedTest;

    @Before
    public void setUp() {
        normalTest = new pingis.entities.Test("test plus operation");
        protectedTest = new pingis.entities.Test();
    }

    @Test
    public void testTestCode() {
        assertEquals("test plus operation", normalTest.getCode());
    }

    @Test
    public void testTestRating() {
        final double RATING = 0.001;
        assertEquals(0, normalTest.getRating(), RATING);
    }

}
