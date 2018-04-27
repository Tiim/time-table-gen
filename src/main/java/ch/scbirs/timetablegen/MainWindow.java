package ch.scbirs.timetablegen;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    TimeTableModel tableModel;

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

        generate.addActionListener(l -> preview());

        buttons.add(load);
        buttons.add(save);
        buttons.add(generate);

        JPanel border = new JPanel(new BorderLayout());
        border.add(box, BorderLayout.CENTER);
        border.add(buttons, BorderLayout.NORTH);
        setContentPane(border);
        pack();
    }

    public void preview() {
        Model m = tableModel.getModel();
        Preview p = new Preview(m);
    }

    private Component getTable() {

        JPanel t = new JPanel(new BorderLayout());

        tableModel = new TimeTableModel(new Model());
        JTable table = new JTable(tableModel);
        t.add(table.getTableHeader(), BorderLayout.NORTH);
        t.add(table, BorderLayout.CENTER);

        TimeCellEditor tce = new TimeCellEditor();
        table.getColumnModel().getColumn(1).setCellEditor(tce);
        table.getColumnModel().getColumn(2).setCellEditor(tce);


        return t;
    }
}
