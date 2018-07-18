package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.Model;
import ch.scbirs.timetablegen.Persistence;
import ch.scbirs.timetablegen.util.ChangeListener;
import ch.scbirs.timetablegen.util.Lang;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    private FileComboBoxModel filesModel;
    private TimeTableModel tableModel;
    private Persistence p;

    private JTextField name;


    public MainWindow() {
        setSize(500, 350);
        p = new Persistence();

        setTitle(Lang.translate("window.main.Name") + " " + p.getFolder());

        init();
        loadFirst();
    }

    private void loadFirst() {
        selectFile();
    }

    private void updateRowHeights(JTable table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            int rowHeight = table.getRowHeight();

            for (int column = 0; column < table.getColumnCount(); column++) {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            table.setRowHeight(row, rowHeight);
        }
    }

    private void init() {
        Component box = getTable();

        JPanel buttons = new JPanel(new FlowLayout());
        JButton changeFolder = new JButton(Lang.translate("button.ChangeFolder"));
        JButton save = new JButton(Lang.translate("button.Save"));
        JButton generate = new JButton(Lang.translate("button.Generate"));

        changeFolder.addActionListener(l -> changeFolder());
        save.addActionListener(l -> save());
        generate.addActionListener(l -> preview());

        buttons.add(changeFolder);
        buttons.add(save);
        buttons.add(generate);

        name = new JTextField(Lang.translate("name.default"));
        ChangeListener.add(name, this::setFileName);

        JComboBox<File> files = new JComboBox<>();
        files.setRenderer(new FileComboBoxRenderer());

        filesModel = new FileComboBoxModel();
        try {
            filesModel.set(p.getFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        //pack();
    }

    private void changeFolder() {
        JFileChooser f = new JFileChooser(".");
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int i = f.showOpenDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = f.getSelectedFile();
            p.setFolder(file.toPath());
            try {
                filesModel.set(p.getFiles());
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadFirst();
        }
        setTitle(Lang.translate("window.main.Name") + " " + p.getFolder());
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

        } catch (IOException e1) {
            System.out.println("Can't load file " + e1.getMessage());
        }
    }

    private void setFileName(String s) {
        tableModel.getModel().setName(s);
    }

    private void save() {
        JFileChooser c = new JFileChooser(p.getFolder().toFile());
        Model model = tableModel.getModel();
        c.setSelectedFile(new File(model.getFileName() + ".json"));
        int i = c.showSaveDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File toSave = c.getSelectedFile();
            String name = FilenameUtils.getBaseName(c.getSelectedFile().toString());
            if (!name.equals(model.getFileName())) {
                model.setName(name);
            }
            File f = p.save(model, toSave.toPath()).toFile();
            try {
                filesModel.set(p.getFiles());
            } catch (IOException e) {
                e.printStackTrace();
            }
            filesModel.setSelectedItem(f);
        }
    }

    public void preview() {
        Model m = tableModel.getModel();
        Preview prev = new Preview(m, p.getFolder());
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

        tableModel.addTableModelListener(l -> updateRowHeights(table));

        return new JScrollPane(t);
    }
}
