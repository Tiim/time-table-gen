package ch.scbirs.timetablegen;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeTableModel implements TableModel {

    private static final String[] HEADER = {"Day", "From", "Until"};
    private static final Class<?>[] CLASSES = {String.class, LocalTime.class, LocalTime.class};

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
        return 3;
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
        return true /*columnIndex >= 1*/;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return Model.DAYS[rowIndex];
        } else if (columnIndex == 1) {
            return model.getRange(rowIndex).getStart();
        } else if (columnIndex == 2) {
            return model.getRange(rowIndex).getEnd();
        }
        throw new IllegalArgumentException("row can't be bigger than 2");
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            Model.TimeRange range = model.getRange(rowIndex);
            model.setRange(new Model.TimeRange((LocalTime) aValue, range.getEnd()), rowIndex);
        } else if (columnIndex == 2) {
            Model.TimeRange range = model.getRange(rowIndex);
            model.setRange(new Model.TimeRange(range.getEnd(), (LocalTime) aValue), rowIndex);
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
}
