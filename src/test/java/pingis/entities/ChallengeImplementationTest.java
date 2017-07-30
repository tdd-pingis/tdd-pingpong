/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ChallengeImplementationTest {
    
    ChallengeImplementation chImp1, protectedChImp;
    private static long incrUserId = 1;
    
    private static final int TMC_USER_LEVEL = 100;
    private static final int USER_1_LEVEL = 3;
    private static final int USER_2_LEVEL = 10;
    
    private User author, implementator, tester;
    
    public ChallengeImplementationTest() {}
    
    @Before
    public void setUp() {
        protectedChImp = new ChallengeImplementation();
        generateTestData();
    }
    
    private void generateTestData() {
        author          = new User(incrUserId++, "ModelUser",     TMC_USER_LEVEL);
        implementator   = new User(incrUserId++, "Implementator", USER_1_LEVEL  );
        tester          = new User(incrUserId++, "Tester",        USER_2_LEVEL  );
        Challenge chal1 = new Challenge("Calculator", author, "Calculator description.");
        chImp1          = new ChallengeImplementation(chal1, tester, implementator);
        
        Task task = new Task(1, author, "TestAddition", "Test addition for two integers",
                "Test_code here a lot of.", 1, 0);

        TaskImplementation[] taskImplementations = generateTaskImplementations(task);
        
        for (TaskImplementation taskImplementation : taskImplementations) {
            // Set same task for TaskImplementations
            taskImplementation.setTask(task);
            if (taskImplementation.getType() == ImplementationType.TEST) {
                taskImplementation.setUser(tester);
            } else {
                taskImplementation.setUser(implementator);
            }
            chImp1.addTaskImplementation(taskImplementation);
        }
    }
    
    private TaskImplementation[] generateTaskImplementations(Task task) {
        TaskImplementation testImp1 
                = new TaskImplementation(implementator, 
                                        "Random testcode", 
                                        ImplementationType.TEST, 
                                        task
                                        );
        TaskImplementation imp1 
                = new TaskImplementation(tester, 
                                        "Random code implementation", 
                                        ImplementationType.IMPLEMENTATION, 
                                        task
                                        );
        return new TaskImplementation[]{ testImp1, imp1 };
    }

    @Test
    public void testConstructor() {
        String noNullMessage = " should NOT return null-value";
        assertNotNull("getChallenge" + noNullMessage, chImp1.getChallenge());
        assertNotNull("getImplementationUser" + noNullMessage, chImp1.getImplementationUser());
        assertNotNull("getTestUser" + noNullMessage, chImp1.getTestUser());
        assertNotNull("getStatus" + noNullMessage, chImp1.getStatus());
        assertNotNull("getTaskImplementations" + noNullMessage, chImp1.getTaskImplementations());
    }
    
    @Test
    public void testStatusByDefaultInProgress() {
        assertEquals(CodeStatus.IN_PROGRESS, chImp1.getStatus());
    }
    
    @Test
    public void testSetStatus() {
        chImp1.setStatus(CodeStatus.DONE);
        assertEquals(CodeStatus.DONE, chImp1.getStatus());
    }
    
    @Test
    public void testSetCompleted() {
        chImp1.setCompleted();
        assertEquals(CodeStatus.DONE, chImp1.getStatus());
    }

    @Test
    public void testGetStatus() {
        assertEquals(CodeStatus.IN_PROGRESS, chImp1.getStatus());
    }
    
    @Test
    public void testGetTestUser() {
        assertEquals(tester, chImp1.getTestUser());
    }
    
    @Test
    public void testGetImplementationUser() {
        assertEquals(implementator, chImp1.getImplementationUser());
    }
    
    @Test
    public void testSetTestUser() {
        User user = new User(incrUserId++, "Tester", 1);
        chImp1.setTestUser(user);
        assertEquals(user, chImp1.getTestUser());
    }
    
    @Test
    public void testSetImplementationUserCannotSetSameAsTester() {
        chImp1.setImplementationUser(tester); // try to set user into both roles
        assertThat(chImp1.getImplementationUser()).isEqualTo(implementator);
    }

    @Test
    public void testSetTestUserCannotSetSameAsImplementator() {
        chImp1.setTestUser(implementator); 
        assertThat(chImp1.getTestUser()).isEqualTo(tester);
    }
}
