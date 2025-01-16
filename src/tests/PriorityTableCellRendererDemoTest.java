package tests;

import App.PriorityTableCellRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PriorityTableCellRendererDemoTest {

    private PriorityTableCellRenderer renderer;
    private JTable table;
    private Object[][] data;

    @BeforeEach
    void setUp() {
        // Initialize the renderer
        renderer = new PriorityTableCellRenderer();

        // Create mock data for the JTable
        data = new Object[][]{
                {"Task 1", "Description 1", "Category 1", "Important", false}, // Important, not completed
                {"Task 2", "Description 2", "Category 2", "Normal", false},    // Normal, not completed
                {"Task 3", "Description 3", "Category 3", "Low", false},       // Low, not completed
                {"Task 4", "Description 4", "Category 4", "Important", true},  // Important, completed
                {"Task 5", "Description 5", "Category 5", "Normal", true}      // Normal, completed
        };

        // Define table structure
        String[] columnNames = {"Task", "Description", "Category", "Priority", "Completed"};
        table = new JTable(data, columnNames);
    }

    @Test
    void testImportantTaskNotCompleted() {
        Component cell = renderer.getTableCellRendererComponent(table, "Important", false, false, 0, 3);
        assertEquals(Color.RED, cell.getBackground(), "Important tasks should have a red background.");
    }

    @Test
    void testNormalTaskNotCompleted() {
        Component cell = renderer.getTableCellRendererComponent(table, "Normal", false, false, 1, 3);
        assertEquals(Color.GREEN, cell.getBackground(), "Normal tasks should have a green background.");
    }

    @Test
    void testLowPriorityTaskNotCompleted() {
        Component cell = renderer.getTableCellRendererComponent(table, "Low", false, false, 2, 3);
        assertEquals(Color.WHITE, cell.getBackground(), "Low-priority tasks should have a white background.");
    }

    @Test
    void testImportantTaskCompleted() {
        Component cell = renderer.getTableCellRendererComponent(table, "Important", false, false, 3, 3);
        assertEquals(Color.LIGHT_GRAY, cell.getBackground(), "Completed tasks should have a light gray background.");
    }

    @Test
    void testNormalTaskCompleted() {
        Component cell = renderer.getTableCellRendererComponent(table, "Normal", false, false, 4, 3);
        assertEquals(Color.LIGHT_GRAY, cell.getBackground(), "Completed tasks should have a light gray background.");
    }

    @Test
    void testSelectionHighlighting() {
        Component cell = renderer.getTableCellRendererComponent(table, "Normal", true, false, 1, 3);
        assertEquals(table.getSelectionBackground(), cell.getBackground(), "Selected tasks should retain the selection background.");
        assertEquals(table.getSelectionForeground(), cell.getForeground(), "Selected tasks should retain the selection foreground.");
    }

    @Test
    void testForegroundDefault() {
        Component cell = renderer.getTableCellRendererComponent(table, "Low", false, false, 2, 3);
        assertEquals(Color.BLACK, cell.getForeground(), "Non-selected tasks should have black text.");
    }
}
