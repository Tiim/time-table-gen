package ch.scbirs.timetablegen;

import com.google.common.primitives.Ints;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.LocalTime;

public class TimeCellEditor extends AbstractCellEditor implements TableCellEditor {

    JTextField component = new JTextField();

    @Override
    public Object getCellEditorValue() {
        String text = component.getText();
        String[] split = text.split(":");
        return LocalTime.of(
                Ints.constrainToRange(Integer.parseInt(split[0]), 0, 23),
                Ints.constrainToRange(Integer.parseInt(split[1]), 0, 59));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        assert value instanceof LocalTime;
        LocalTime v = (LocalTime) value;
        component.setText(v.toString());
        return component;
    }
}
