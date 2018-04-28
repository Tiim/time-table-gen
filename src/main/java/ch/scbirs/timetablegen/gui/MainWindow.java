package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.*;
import ch.scbirs.timetablegen.util.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

    private TimeTableModel tableModel;
    private Persistence p;


    public MainWindow() {
        setSize(500, 500);
        p = new Persistence();

        init();
    }

    private void init() {
        Component box = getTable();

        JPanel buttons = new JPanel(new FlowLayout());
        JButton load = new JButton("Load");
        JButton save = new JButton("Save");
        JButton generate = new JButton("Generate");

        save.addActionListener(l -> save());
        generate.addActionListener(l -> preview());

        buttons.add(load);
        buttons.add(save);
        buttons.add(generate);

        JTextField name = new JTextField("name.default");
        ChangeListener.add(name, this::setFileName);

        JComboBox<File> files = new JComboBox<>();
        loadFiles(files);
        files.addActionListener(this::selectFile);

        JPanel border = new JPanel(new BorderLayout());
        JPanel border2 = new JPanel(new BorderLayout());
        border2.add(box, BorderLayout.CENTER);
        border.add(border2, BorderLayout.CENTER);
        border2.add(name, BorderLayout.SOUTH);
        border2.add(files, BorderLayout.NORTH);
        border.add(buttons, BorderLayout.NORTH);
        setContentPane(border);
        pack();
    }

    private void selectFile(ActionEvent e) {
        JComboBox<File> s = (JComboBox<File>) e.getSource();
        Model m = null;
        try {
            m = p.load(((File) s.getSelectedItem()));
            tableModel.setModel(m);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void loadFiles(JComboBox<File> files) {
        File[] f = p.getFiles();
        DefaultComboBoxModel<File> m = new DefaultComboBoxModel<>(f);
        files.setModel(m);
    }

    private void setFileName(String s) {
        tableModel.getModel().setName(s);
    }

    private void save() {
        p.save(tableModel.getModel());
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
