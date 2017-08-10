package pingis.services;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.Application;
import pingis.entities.Task;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import pingis.entities.TaskType;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepositoryMock;

    @MockBean
    private ChallengeRepository challengeRepositoryMock;

    private User testUser;
    private Task testTask;
    private ArgumentCaptor<Task> taskCaptor;

    @Before
    public void setUp() {
        testUser = new User(1, "Matti", 1);
        testTask = new Task(1, TaskType.TEST, testUser, "FirstTask", "SimpleCalcluator", "return 0;", 1, 1);
        taskCaptor = ArgumentCaptor.forClass(Task.class);
    }


    @Test
    public void simpleSaveAndFindOneTaskTest() {
        taskService.save(testTask);
        taskService.findOne(testTask.getId());

        verify(taskRepositoryMock, times(1)).save(taskCaptor.capture());
        verify(taskRepositoryMock, times(1)).findOne(taskCaptor.getValue().getId());
        verifyNoMoreInteractions(taskRepositoryMock);

        Task oneTask = taskCaptor.getValue();

        assertEquals(oneTask.getName(), testTask.getName());
    }

    @Test
    public void simpleFindAllTaskTest() {
        taskService.save(testTask);

        List<Task> testTasks = new ArrayList<Task>();
        testTasks.add(testTask);

        when(taskRepositoryMock.findAll()).thenReturn(testTasks);
        List<Task> found = taskService.findAll();

        verify(taskRepositoryMock, times(1)).findAll();
        assertEquals(found.size(), testTasks.size());
    }

    @Test
    public void simpleDeleteOneTaskTest() {
        taskService.save(testTask);

        verify(taskRepositoryMock, times(1)).save(taskCaptor.capture());

        taskService.delete(taskCaptor.getValue().getId());

        verify(taskRepositoryMock, times(1)).delete(taskCaptor.getValue().getId());

        boolean deleted = taskService.contains(taskCaptor.getValue().getId());

        assertFalse(deleted);
    }

}
