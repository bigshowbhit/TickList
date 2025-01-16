package App;

import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private String dueDate;
    private String priority;
    private boolean completed;

    public Task(String title, String description, String dueDate, String priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
