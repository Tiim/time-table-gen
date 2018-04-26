package ch.scbirs.timetablegen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedImage generate = new Generator(new Model()).generate();
        ImageIO.write(generate, "PNG", new File("test.png"));

        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}
