package App;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;
    private static final String FILE_PATH = "tasks.ser";  // File for persistence

    public TaskList() {
        tasks = loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    //Serialization: Adding to the file
    public void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(tasks);
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Deserialization: Load the task list from the file
    @SuppressWarnings("unchecked")
    public List<Task> loadTasks() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                return (List<Task>) ois.readObject();   //It puts the object back to the list
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(); // Return an empty list if no tasks are saved
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
