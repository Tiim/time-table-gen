package ch.scbirs.timetablegen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedImage generate = new Generator(new Model()).generate();
        ImageIO.write(generate, "PNG", new File("test.png"));

        JFrame jFrame = new JFrame();
        jFrame.setContentPane(new JLabel(new ImageIcon(generate)));
        jFrame.setVisible(true);
        jFrame.pack();

//        MainWindow window = new MainWindow();
//        window.setVisible(true);
    }
}
