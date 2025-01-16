package tests;

import App.Task;
import App.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTestDemo {

    private TaskManager taskManager;
    private String testFileName;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
        testFileName = "test_tasks.dat";
    }

    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File file = new File(testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddTask() {
        Task task = new Task("Task 1", "Description 1", "2024-12-31", "Important", false);
        taskManager.addTask(task);

        ArrayList<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Task list size should be 1 after adding a task.");
        assertEquals(task, tasks.get(0), "The added task should match the retrieved task.");
    }

    @Test
    void testDeleteTask() {
        Task task1 = new Task("Task 1", "Description 1", "2024-12-31", "Important", false);
        Task task2 = new Task("Task 2", "Description 2", "2024-11-30", "Normal", true);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.deleteTask(0);

        ArrayList<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Task list size should be 1 after deleting a task.");
        assertEquals(task2, tasks.get(0), "Remaining task should match the second task.");
    }

    @Test
    void testEditTask() {
        Task originalTask = new Task("Task 1", "Description 1", "2024-12-31", "Important", false);
        Task updatedTask = new Task("Updated Task", "Updated Description", "2025-01-01", "Normal", true);
        taskManager.addTask(originalTask);

        taskManager.editTask(0, updatedTask);

        ArrayList<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Task list size should remain 1 after editing.");
        assertEquals(updatedTask, tasks.get(0), "The edited task should match the updated task.");
    }

    @Test
    void testSaveAndLoadTasks() throws IOException, ClassNotFoundException {
        Task task1 = new Task("Task 1", "Description 1", "2024-12-31", "Important", false);
        Task task2 = new Task("Task 2", "Description 2", "2024-11-30", "Normal", true);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.saveTasksToFile(testFileName);

        TaskManager loadedTaskManager = new TaskManager();
        loadedTaskManager.loadTasksFromFile(testFileName);

        ArrayList<Task> loadedTasks = loadedTaskManager.getTasks();
        assertEquals(2, loadedTasks.size(), "Loaded task list size should match the saved list size.");
        assertEquals(task1.getTitle(), loadedTasks.get(0).getTitle(), "First loaded task title should match the first saved task.");
        assertEquals(task2.getPriority(), loadedTasks.get(1).getPriority(), "Second loaded task priority should match the second saved task.");
    }

    @Test
    void testDeleteTaskOutOfBounds() {
        Task task = new Task("Task 1", "Description 1", "2024-12-31", "Important", false);
        taskManager.addTask(task);

        taskManager.deleteTask(5); // Out of bounds
        assertEquals(1, taskManager.getTasks().size(), "Task list size should remain unchanged for out-of-bounds index.");
    }

    @Test
    void testEditTaskOutOfBounds() {
        Task originalTask = new Task("Task 1", "Description 1", "2024-12-31", "Important", false);
        Task updatedTask = new Task("Updated Task", "Updated Description", "2025-01-01", "Normal", true);
        taskManager.addTask(originalTask);

        taskManager.editTask(5, updatedTask); // Out of bounds
        assertEquals(originalTask, taskManager.getTasks().get(0), "Task should remain unchanged for out-of-bounds index.");
    }
}
