package tests;

import App.Task;
import App.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TickListTestDemo {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Test Task", "Description", "20241129", "Normal", false);
        taskList.addTask(task);

        assertEquals(1, taskList.getTasks().size());
        assertEquals("Test Task", taskList.getTasks().get(0).getTitle());
    }

    @Test
    public void testEditTask() {
        Task task = new Task("Test Task", "Description", "20241129", "Normal", false);
        taskList.addTask(task);

        // Update task
        task.setDescription("Updated Description");
        task.setDueDate("20241130");
        task.setPriority("Important");

        assertEquals("Updated Description", taskList.getTasks().get(0).getDescription());
        assertEquals("20241130", taskList.getTasks().get(0).getDueDate());
        assertEquals("Important", taskList.getTasks().get(0).getPriority());
    }

    @Test
    public void testDeleteTask() {
        Task task1 = new Task("Task 1", "Description 1", "20241129", "Normal", false);
        Task task2 = new Task("Task 2", "Description 2", "20241201", "Important", true);
        taskList.addTask(task1);
        taskList.addTask(task2);

        taskList.getTasks().removeIf(task -> task.getTitle().equals("Task 1"));

        assertEquals(1, taskList.getTasks().size());
        assertEquals("Task 2", taskList.getTasks().get(0).getTitle());
    }

    @Test
    public void testReminderForDueTasks() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Create tasks with due dates
        String today = sdf.format(new Date());
        String tomorrow = sdf.format(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));

        Task task1 = new Task("Due Soon", "Description", today, "Normal", false);
        Task task2 = new Task("Completed Task", "Description", tomorrow, "Normal", true);

        TaskList taskList = new TaskList(); // Ensure taskList is initialized
        taskList.addTask(task1);
        taskList.addTask(task2);

        // Filter tasks due within the next 24 hours
        long tasksDueSoon = taskList.getTasks().stream()
                .filter(task -> {
                    try {
                        Date dueDate = sdf.parse(task.getDueDate());
                        Date now = new Date();

                        // Normalize dueDate to end-of-day and compare
                        dueDate = new Date(dueDate.getTime() + (1000 * 60 * 60 * 24 - 1));

                        long timeDifference = dueDate.getTime() - now.getTime();
                        long hoursDifference = timeDifference / (1000 * 60 * 60);

                        return hoursDifference <= 24 && hoursDifference >= 0 && !task.isCompleted();
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .count();

        // Assert that only 1 task is due soon
        assertEquals(1, tasksDueSoon);
    }


    @Test
    public void testDeleteAllTasks() {
        Task task1 = new Task("Task 1", "Description 1", "20241129", "Normal", false);
        Task task2 = new Task("Task 2", "Description 2", "20241201", "Important", true);
        taskList.addTask(task1);
        taskList.addTask(task2);

        taskList.getTasks().clear();

        assertEquals(0, taskList.getTasks().size());
    }
}
