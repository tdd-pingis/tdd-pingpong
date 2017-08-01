package pingis.entities;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChallengeTest {

    Challenge calculatorChallenge, protectedChallenge;
    private User authorUser;
    private static final int TMC_USER_LEVEL = 100;

    @Before
    public void setUp() {
        authorUser = new User(1, "ModelUser", TMC_USER_LEVEL);
        calculatorChallenge = new Challenge("Calculator", authorUser, "Calculator description.",
                "src/Calculator.java", "test/CalculatorTest.java");
        protectedChallenge = new Challenge();
    }

    @Test
    public void testChallengeToString() {
        assertEquals("Calculator: Calculator description.", calculatorChallenge.toString());
    }
    
}