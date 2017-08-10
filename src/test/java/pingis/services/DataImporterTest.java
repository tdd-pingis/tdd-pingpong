package pingis.services;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import static pingis.services.DataImporter.UserType.TEST_USER;
import static pingis.services.DataImporter.UserType.TMC_MODEL_USER;
import static pingis.services.DataImporter.UserType.IMPLEMENTATION_USER;

public class DataImporterTest {

    private DataImporter importer;
    private IOStub io;

    public DataImporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.importer = new DataImporter();
        this.io = new IOStub();
        importer.readData(io);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReadData() {
        assertTrue(importer.getJsonString() != null);
    }

    @Test
    public void testGenerateUsers() {
        importer.generateUsers();
        HashMap<String, User> users = importer.getUsers();
        assertEquals(users.get("testuser").getName(), TEST_USER.getLogin());
        assertEquals(users.get("modeluser").getName(), TMC_MODEL_USER.getLogin());
        assertEquals(users.get("impluser").getName(), IMPLEMENTATION_USER.getLogin());
    }

    @Test
    public void testGenerateEntities() {
        importer.generateUsers();
        importer.generateEntities();
        HashMap<String, Challenge> challenges = importer.getChallenges();

        ArrayList<Task> tasks = importer.getTasks();
        ArrayList<TaskInstance> taskInstances = importer.getTaskInstances();

        assertEquals(challenges.get("calculator").getName(), "calculator");
        assertEquals(tasks.get(0).getName(), "Test multiplication");
        assertEquals(taskInstances.get(0).getTask(), tasks.get(0));
    }

}
