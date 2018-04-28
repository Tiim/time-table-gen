package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.Model;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeTableModel implements TableModel {

    private static final String[] HEADER = {"Day", "From", "Until", "Enabled"};
    private static final Class<?>[] CLASSES = {String.class, LocalTime.class, LocalTime.class, Boolean.class};

    private final List<TableModelListener> listeners = new ArrayList<>();

    private Model model;

    public TimeTableModel(Model model) {
        this.model = model;
    }


    @Override
    public int getRowCount() {
        return Model.DAYS.length;
    }

    @Override
    public int getColumnCount() {
        return HEADER.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return HEADER[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CLASSES[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return Model.DAYS[rowIndex];
        } else if (columnIndex == 1) {
            return model.getRange(rowIndex).getStart();
        } else if (columnIndex == 2) {
            return model.getRange(rowIndex).getEnd();
        } else if (columnIndex == 3) {
            return model.getRange(rowIndex).isEnabled();
        }
        throw new IllegalArgumentException("row can't be bigger than " + (HEADER.length - 1));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            Model.TimeRange range = model.getRange(rowIndex);
            model.setRange(new Model.TimeRange((LocalTime) aValue, range.getEnd(), range.isEnabled()), rowIndex);
        } else if (columnIndex == 2) {
            Model.TimeRange range = model.getRange(rowIndex);
            model.setRange(new Model.TimeRange(range.getStart(), (LocalTime) aValue, range.isEnabled()), rowIndex);
        } else if (columnIndex == 3) {
            Model.TimeRange range = model.getRange(rowIndex);
            model.setRange(new Model.TimeRange(range.getStart(), range.getEnd(), (Boolean) aValue), rowIndex);
        }
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex)));
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        if (model == null) {
            model = new Model();
        }
        this.model = model;
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this)));
    }
}
