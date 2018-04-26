package ch.scbirs.timetablegen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Generator {

    private static final int WIDTH = 1800, HEIGHT = 800;
    private static final Font FONT = new Font("Arial", Font.PLAIN, 80);

    private final Model model;

    public Generator(Model model) {

        this.model = model;
    }

    public BufferedImage generate() {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(FONT);
        g.setPaint(Color.white);
        g.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
        g.setPaint(Color.black);
        drawGrid(g);
        drawFill(g);
        drawText(g);
        return img;
    }

    private void drawText(Graphics2D g) {

    }

    private void drawFill(Graphics2D g) {

    }

    private void drawGrid(Graphics2D g) {
        int rowh = HEIGHT / 9;
        int colw = WIDTH / 8;

        g.setStroke(new BasicStroke(HEIGHT/500));

        for (int i = 0; i < 9; i++) {
            int y = (i + 1) * rowh;
            g.drawLine(0, y, WIDTH, y);
        }

    }
}
