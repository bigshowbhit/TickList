package App;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TickListApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;   //Just like ViewModel in Android, it manages data inside the table
    private TaskList taskList;  // TaskList for managing tasks - Task related Information

    public TickListApp() {
        // Initializing the TaskList
        taskList = new TaskList();

        // Initialize the JFrame
        frame = new JFrame("TickList - To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Initialize the table and model
        String[] columnNames = {"Title", "Description", "Due Date", "Priority", "Completed"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { //As Completed is a checkbox, it will return a boolean
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; //Allowing the completed column as editable only.
            }
        };
        table = new JTable(tableModel);

        table.setDefaultRenderer(Object.class, new PriorityTableCellRenderer());


        // Add a listener to handle changes in the "Completed" column
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Check if the "Completed" column was updated
            if (column == 4) {
                Boolean isCompleted = (Boolean) tableModel.getValueAt(row, column);

                // Find the corresponding Task in the TaskList
                String taskTitle = (String) tableModel.getValueAt(row, 0); // Assuming title is unique
                for (Task task : taskList.getTasks()) {
                    if (task.getTitle().equals(taskTitle)) {
                        task.setCompleted(isCompleted); // Update the Task object
                        taskList.saveTasks(); // Save the updated TaskList to persist changes
                        break;
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton saveButton = new JButton("Save Tasks");
        JButton deleteAllButton = new JButton("Delete All Tasks");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteAllButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTask());

        editButton.addActionListener(e -> editTask());

        deleteButton.addActionListener(e -> deleteTask());

        saveButton.addActionListener(e -> {
            saveTasks();  // Call saveTasks when the button is clicked
        });

        deleteAllButton.addActionListener(e -> {
            deleteAllTasks();  // Call deleteAllTasks when the button is clicked
        });

        loadTasksToUI();

        startReminderChecker();

        frame.setVisible(true);
    }

    private void startReminderChecker() {
        // Run the reminder checker every 1 minute
        Timer timer = new Timer(60000, e -> checkForDueTasks());
        timer.start();
    }

    private void addTask() {
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();

        // Use JFormattedTextField for due date with NumberFormatter so as to restrict the user to enter non numeric characters
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false); // No invalid characters
        JFormattedTextField dueDateField = new JFormattedTextField(numberFormatter);
        dueDateField.setColumns(8);

        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"Important", "Normal"});
        JCheckBox completedCheckBox = new JCheckBox("Completed");

        //a dialog
        Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Due Date (yyyyMMdd):", dueDateField,
                "Priority:", priorityComboBox,
                completedCheckBox
        };

        // Showing the dialog
        int option = JOptionPane.showConfirmDialog(frame, message, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String dueDate = dueDateField.getText().trim();
            String priority = (String) priorityComboBox.getSelectedItem();
            boolean completed = completedCheckBox.isSelected();

            // Validate mandatory fields
            if (title.isEmpty() || dueDate.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Title and Due Date are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Task newTask = new Task(title, description, dueDate, priority, completed);
            taskList.addTask(newTask);

            tableModel.addRow(new Object[]{title, description, dueDate, priority, completed});

            taskList.saveTasks();
        }
    }

    private void editTask() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a task to edit.");
            return;
        }

        // Retrieve the task's current details
        String currentTitle = (String) table.getValueAt(selectedRow, 0);
        String currentDescription = (String) table.getValueAt(selectedRow, 1);
        String currentDueDate = (String) table.getValueAt(selectedRow, 2);
        String currentPriority = (String) table.getValueAt(selectedRow, 3);
        Boolean currentCompleted = (Boolean) table.getValueAt(selectedRow, 4);

        // Create input fields pre-filled with the current task details
        JTextField titleField = new JTextField(currentTitle);
        JTextField descriptionField = new JTextField(currentDescription);
        JTextField dueDateField = new JTextField(currentDueDate);
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"Important", "Normal"});
        priorityComboBox.setSelectedItem(currentPriority);
        JCheckBox completedCheckBox = new JCheckBox("Completed", currentCompleted);

        Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Due Date (yyyyMMdd):", dueDateField,
                "Priority:", priorityComboBox,
                completedCheckBox
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newTitle = titleField.getText().trim();
            String newDescription = descriptionField.getText().trim();
            String newDueDate = dueDateField.getText().trim();
            String newPriority = (String) priorityComboBox.getSelectedItem();
            boolean newCompleted = completedCheckBox.isSelected();

            //Validating the mandatory fields
            if (newTitle.isEmpty() || newDueDate.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Title and Due Date are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Updating the Task in the TaskList
            for (Task task : taskList.getTasks()) {
                if (task.getTitle().equals(currentTitle)) {
                    taskList.getTasks().remove(task); // Remove old task
                    Task updatedTask = new Task(newTitle, newDescription, newDueDate, newPriority, newCompleted);
                    taskList.getTasks().add(updatedTask); // Add updated task
                    break;
                }
            }

            // Update the table
            tableModel.setValueAt(newTitle, selectedRow, 0);
            tableModel.setValueAt(newDescription, selectedRow, 1);
            tableModel.setValueAt(newDueDate, selectedRow, 2);
            tableModel.setValueAt(newPriority, selectedRow, 3);
            tableModel.setValueAt(newCompleted, selectedRow, 4);

            // Save changes to file
            taskList.saveTasks();

            JOptionPane.showMessageDialog(frame, "Task updated successfully.");
        }
    }


    private void deleteTask() {
        // Delete the selected task
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String taskTitle = (String) table.getValueAt(selectedRow, 0);  //Takes the title and checks in the respected field.

            // Find the task in TaskList and remove it
            for (Task task : taskList.getTasks()) {
                if (task.getTitle().equals(taskTitle)) {
                    taskList.getTasks().remove(task);
                    break;
                }
            }

            tableModel.removeRow(selectedRow);

            taskList.saveTasks();

            JOptionPane.showMessageDialog(frame, "Task deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to delete.");
        }
    }

    private void saveTasks() {
        taskList.saveTasks();
        JOptionPane.showMessageDialog(frame, "Tasks saved successfully.");
    }

    private void loadTasksToUI() {
        for (Task task : taskList.getTasks()) {
            tableModel.addRow(new Object[]{task.getTitle(), task.getDescription(), task.getDueDate(), task.getPriority(), task.isCompleted()});
        }
    }

    private void deleteAllTasks() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete all tasks?", "Confirm Delete All", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {

            taskList.getTasks().clear();

            tableModel.setRowCount(0);

            taskList.saveTasks();

            JOptionPane.showMessageDialog(frame, "All tasks deleted successfully.");
        }
    }


    private void checkForDueTasks() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // Assuming due date is in YYYYMMDD format

        for (Task task : taskList.getTasks()) {
            try {
                Date dueDate = sdf.parse(task.getDueDate());

                dueDate = new Date(dueDate.getTime() + (1000 * 60 * 60 * 24 - 1));

                // Check if the task is due in the next 24 hours
                long timeDifference = dueDate.getTime() - now.getTime();
                long hoursDifference = timeDifference / (1000 * 60 * 60);

                if (hoursDifference <= 24 && hoursDifference >= 0 && !task.isCompleted()) {
                    // Notify the user
                    JOptionPane.showMessageDialog(frame, "Reminder: Task \"" + task.getTitle() + "\" is due soon!", "Task Reminder", JOptionPane.WARNING_MESSAGE);
                }
            } catch (ParseException e) {
                System.err.println("Invalid date format for task: " + task.getTitle());
            }
        }
    }
}