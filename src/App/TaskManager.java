package App;

import java.io.*;
import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    public void editTask(int index, Task updatedTask) {
        if (index >= 0 && index < tasks.size()) {
            tasks.set(index, updatedTask);
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void saveTasksToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tasks);
        }
    }

    public void loadTasksFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            tasks = (ArrayList<Task>) ois.readObject();
        }
    }
}
