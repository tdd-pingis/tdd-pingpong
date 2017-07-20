package entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChallengeTest {

    Challenge calculatorChallenge, protectedChallenge;

    @Before
    public void setUp() {
        calculatorChallenge = new Challenge("Calculator");
        protectedChallenge = new Challenge();
    }

    @Test
    public void testChallengeToString() {
        assertEquals("Calculator", calculatorChallenge.toString());
    }

}
