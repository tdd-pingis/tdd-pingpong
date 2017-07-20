package entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestTest {

    entities.Test normalTest, protectedTest;

    @Before
    public void setUp() {
        normalTest = new entities.Test("test plus operation");
        protectedTest = new entities.Test();
    }

    @Test
    public void testTestCode() {
        assertEquals("test plus operation", normalTest.getCode());
    }

    @Test
    public void testTestRating() {
        assertEquals(0, normalTest.getRating(),0.001);
    }

}
