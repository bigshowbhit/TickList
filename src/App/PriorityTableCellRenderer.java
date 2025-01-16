package App;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PriorityTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Calling the parent method to get the default renderer
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String priority = (String) table.getValueAt(row, 3);  // Priority is in the 4th column (index 3)
        Boolean completed = (Boolean) table.getValueAt(row, 4);  // Completed is in the 5th column (index 4)

        if (Boolean.TRUE.equals(completed)) {
            cell.setBackground(Color.LIGHT_GRAY); // Gray for completed tasks
        } else if ("Important".equals(priority)) {
            cell.setBackground(Color.RED); // Red for important tasks
        } else if ("Normal".equals(priority)) {
            cell.setBackground(Color.GREEN); // Green for normal tasks
        } else {
            cell.setBackground(Color.WHITE); // Default background
        }

        // Retain the selection highlighting if the row is selected
        if (isSelected) {
            cell.setBackground(table.getSelectionBackground());
            cell.setForeground(table.getSelectionForeground());
        } else {
            cell.setForeground(Color.BLACK); //default
        }

        return cell;
    }
}