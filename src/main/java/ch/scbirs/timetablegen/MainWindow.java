package ch.scbirs.timetablegen;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setSize(500, 500);
        init();
    }

    private void init() {
        Component box = getTable();

        JPanel buttons = new JPanel(new FlowLayout());
        JButton load = new JButton("Load");
        JButton save = new JButton("Save");
        JButton generate = new JButton("Generate");

        buttons.add(load);
        buttons.add(save);
        buttons.add(generate);

        JPanel border = new JPanel(new BorderLayout());
        border.add(box, BorderLayout.CENTER);
        border.add(buttons, BorderLayout.NORTH);
        setContentPane(border);
        pack();
    }

    private Component getTable() {

        JPanel t = new JPanel(new BorderLayout());

        JTable table = new JTable(new TimeTableModel(new Model()));
        t.add(table.getTableHeader(), BorderLayout.NORTH);
        t.add(table, BorderLayout.CENTER);

        TimeCellEditor tce = new TimeCellEditor();
        table.getColumnModel().getColumn(1).setCellEditor(tce);
        table.getColumnModel().getColumn(2).setCellEditor(tce);


        return t;
    }
}
