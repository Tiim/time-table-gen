package ch.scbirs.timetablegen.gui;

import ch.scbirs.timetablegen.Generator;
import ch.scbirs.timetablegen.Model;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Preview extends JFrame {

    public Preview(Model model) {
        BufferedImage generate = new Generator(model).generate();

        setContentPane(new JLabel(new ImageIcon(generate.getScaledInstance(500, -1, BufferedImage.SCALE_SMOOTH))));
        setVisible(true);
        pack();
    }
}
