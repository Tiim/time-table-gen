package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.util.ChangeListener;
import com.google.common.primitives.Ints;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.LocalTime;

public class TimeCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextField component = new JTextField();
    private DocumentListener listener;
    private LocalTime lastValue;

    @Override
    public Object getCellEditorValue() {
        String text = component.getText();
        try {
            String[] split = text.split(":");
            switch (split.length) {
                case 1:
                    return LocalTime.of(Ints.constrainToRange(Integer.parseInt(split[0]), 0, 23), 0);
                case 2:
                    return LocalTime.of(
                            Ints.constrainToRange(Integer.parseInt(split[0]), 0, 23),
                            Ints.constrainToRange(Integer.parseInt(split[1]), 0, 59));
                default:
                    return lastValue;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return lastValue;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        assert value instanceof LocalTime;
        ChangeListener.remove(component, listener);
        lastValue = (LocalTime) value;
        component.setText("");
        listener = ChangeListener.add(component, c -> {
            Object cellEditorValue = getCellEditorValue();
            System.out.println("New value " + cellEditorValue);
            table.getModel().setValueAt(cellEditorValue, row, column);
        });
        return component;
    }
}
