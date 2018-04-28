package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.Model;
import ch.scbirs.timetablegen.Persistence;
import ch.scbirs.timetablegen.util.ChangeListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

public class MainWindow extends JFrame {

    private FileComboBoxModel filesModel;
    private TimeTableModel tableModel;
    private Persistence p;

    private JTextField name;


    public MainWindow() {
        setSize(500, 500);
        p = new Persistence();

        init();
        loadFirst();
    }

    private void loadFirst() {
        selectFile();
    }

    private void init() {
        Component box = getTable();

        JPanel buttons = new JPanel(new FlowLayout());
        JButton changeFolder = new JButton("Change folder");
        JButton save = new JButton("Save");
        JButton generate = new JButton("Generate");

        changeFolder.addActionListener(l -> changeFolder());
        save.addActionListener(l -> save());
        generate.addActionListener(l -> preview());

        buttons.add(changeFolder);
        buttons.add(save);
        buttons.add(generate);

        name = new JTextField("name.default");
        ChangeListener.add(name, this::setFileName);

        JComboBox<File> files = new JComboBox<>();

        filesModel = new FileComboBoxModel();
        filesModel.set(p.getFiles());

        if (filesModel.getSize() > 0) {
            filesModel.setSelectedItem(0);
        }
        files.setModel(filesModel);


        files.addActionListener(l -> selectFile());

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

    private void changeFolder() {
        JFileChooser f = new JFileChooser(".");
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int i = f.showOpenDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = f.getSelectedFile();
            p.setFolder(file.toPath());
            filesModel.set(p.getFiles());
            loadFirst();
        }
    }

    private void selectFile() {
        try {
            Model m = null;
            File i = (File) filesModel.getSelectedItem();
            if (i != null) {
                m = p.load(i);
                name.setText(m.getName());
            }
            tableModel.setModel(m);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void setFileName(String s) {
        tableModel.getModel().setName(s);
    }

    private void save() {
        File f = p.save(tableModel.getModel()).toFile();
        String name = tableModel.getModel().getFileName();
        filesModel.set(p.getFiles());
        filesModel.setSelectedItem(f);
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
