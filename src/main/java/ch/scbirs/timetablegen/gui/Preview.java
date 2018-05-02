package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.Generator;
import ch.scbirs.timetablegen.Model;
import ch.scbirs.timetablegen.util.Lang;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Preview extends JFrame {

    private final Model model;
    private final BufferedImage generate;
    private final Path dir;


    public Preview(Model model, Path dir) {
        this.model = model;
        generate = new Generator(model).generate();
        this.dir = dir;

        setTitle(Lang.translate("window.preview.Window"));

        JLabel contentPane = new JLabel(new ImageIcon(generate.getScaledInstance(800, -1, BufferedImage.SCALE_SMOOTH)));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(contentPane, BorderLayout.CENTER);
        JButton save = new JButton(Lang.translate("button.Save"));

        save.addActionListener(l -> save());

        panel.add(save, BorderLayout.SOUTH);
        setContentPane(panel);
        setVisible(true);
        pack();
    }

    private void save() {
        JFileChooser c = new JFileChooser(dir.toFile());
        c.setSelectedFile(new File( model.getFileName() + ".png"));
        int i = c.showSaveDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File f = c.getSelectedFile();
            try {
                ImageIO.write(generate, "PNG", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
