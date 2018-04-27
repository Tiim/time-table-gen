package ch.scbirs.timetablegen;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    private static final int STROKE_WIDTH = 9;

    private static final int[] TIMES_WEEKDAY = {16, 17, 18, 19, 20, 21};
    private static final int[] TIMES_WEEKEND = {13, 14, 15, 16, 17, 18};
    private static final String[] DAYS = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};

    private static final int WEEK_DAYS = 5;

    private static final int DAY_WIDTH = 2, UNIT_WIDTH = 4;
    private static final int COLS = (DAY_WIDTH + TIMES_WEEKDAY.length) * UNIT_WIDTH, ROWS = DAYS.length + 3;
    private static final int WIDTH = 50 * COLS + STROKE_WIDTH, HEIGHT = 100 * ROWS + STROKE_WIDTH;


    private static final Font FONT = new Font("Arial", Font.PLAIN, 80);
    private static final double TEXT_SHRINK = 0.1;

    private final Model model;

    public Generator(Model model) {

        this.model = model;
    }

    public BufferedImage generate() {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHints(rh);
        g.setFont(FONT);
        g.setPaint(Color.white);
        g.fill(new Rectangle(0, 0, WIDTH, HEIGHT));
        g.setPaint(Color.black);
        drawGrid(g);
        drawGridCover(g);
        drawFill(g);
        drawText(g);
        return img;
    }

    private void drawGridCover(Graphics2D g) {
        Color oldColor = g.getColor();
        try {
            g.setColor(Color.WHITE);

            fillCell(g, 0, 0, 8, 1, true, false, true, false);
            fillCell(g, 0, WEEK_DAYS + 2, 8, 1, true, false, true, false);
            // Remove lines from headers
            for (int i = DAY_WIDTH; i < (COLS / UNIT_WIDTH); i++) {
                fillCell(g, UNIT_WIDTH * i, 0, UNIT_WIDTH, 1, false, false, false, false);
                fillCell(g, UNIT_WIDTH * i, WEEK_DAYS + 2, UNIT_WIDTH, 1, false, false, false, false);
            }
            // Remove lines from Day Slot
            for (int i = 0; i < ROWS; i++) {
                fillCell(g, 0, i, DAY_WIDTH * UNIT_WIDTH, 1, false, false, false, false);
            }
            // Remove lines between weekdays and weekend
            fillCell(g, 0, WEEK_DAYS + 1, COLS, 1, false, true, true, false);
        } finally {
            g.setColor(oldColor);
        }
    }

    private void fillCell(Graphics2D g, int x, int y, int w, int h, boolean coverTop, boolean coverRight, boolean coverLeft, boolean coverBottom) {
        Rectangle rect = cellRect(x, y, w, h, coverTop, coverRight, coverLeft, coverBottom);
        g.fill(rect);
    }

    private Rectangle cellRect(int x, int y, int w, int h, boolean coverTop, boolean coverRight, boolean coverLeft, boolean coverBottom) {
        int xInc = (WIDTH - STROKE_WIDTH) / COLS;
        int yInc = (HEIGHT - STROKE_WIDTH) / ROWS;

        int xoff1 = coverLeft ? 0 : STROKE_WIDTH;
        int xoff2 = (coverLeft ? 0 : -STROKE_WIDTH) + (coverRight ? STROKE_WIDTH : 0);
        int yoff1 = coverTop ? 0 : STROKE_WIDTH;
        int yoff2 = (coverTop ? 0 : -STROKE_WIDTH) + (coverBottom ? STROKE_WIDTH : 0);

        return new Rectangle(x * xInc + xoff1, y * yInc + yoff1, w * xInc + xoff2, h * yInc + yoff2);
    }

    private Rectangle cellRect(int x, int y, int w, int h) {
        return cellRect(x, y, w, h, false, false, false, false);
    }

    private Font scaleFont(List<TextRec> recs, Graphics g) {
        float fontSize = g.getFont().getSize2D();
        Font font;

        for (TextRec rec : recs) {
            font = g.getFont().deriveFont(fontSize);
            float width = g.getFontMetrics(font).stringWidth(rec.getText());
            float height = g.getFontMetrics(font).getHeight();
            float rectWidth = rec.getRect().width;
            float rectHeight = rec.getRect().height;

            fontSize = Math.min((rectHeight / height) * fontSize, fontSize);
            fontSize = Math.min((rectWidth / width) * fontSize, fontSize);
        }
        return g.getFont().deriveFont(fontSize);
    }

    private void drawText(Graphics2D g) {
        List<TextRec> recs = getTextRecs();
        System.out.println("Fontsize old: " + g.getFont().getSize2D());
        g.setFont(scaleFont(recs, g));
        System.out.println("Fontsize new: " + g.getFont().getSize2D());

        FontMetrics metrics = g.getFontMetrics();
        for (TextRec rec : recs) {
            Rectangle r = rec.getRect();
            String t = rec.getText();
            int y = r.y + ((r.height - metrics.getHeight()) / 2) + metrics.getAscent();
            int x = r.x + (r.width - metrics.stringWidth(t)) / 2;
            g.drawString(t, x, y);
        }
    }

    private List<TextRec> getTextRecs() {
        List<TextRec> recs = new ArrayList<>();
        for (int i = 0; i < DAYS.length; i++) {
            int j = i < WEEK_DAYS ? i + 1 : i + 3;
            recs.add(new TextRec(
                    DAYS[i],
                    cellRect(0, j, DAY_WIDTH * UNIT_WIDTH, 1)
            ));
        }
        for (int i = 0; i < TIMES_WEEKDAY.length; i++) {
            recs.add(new TextRec(
                    String.format("%d:00", TIMES_WEEKDAY[i]),
                    cellRect((UNIT_WIDTH * DAY_WIDTH) + (UNIT_WIDTH * i), 0, UNIT_WIDTH, 1)
            ));
        }
        for (int i = 0; i < TIMES_WEEKEND.length; i++) {
            recs.add(new TextRec(
                    String.format("%d:00", TIMES_WEEKDAY[i]),
                    cellRect((UNIT_WIDTH * DAY_WIDTH) + (UNIT_WIDTH * i), WEEK_DAYS + 2, UNIT_WIDTH, 1)
            ));
        }
        for (TextRec rec : recs) {
            Rectangle r = rec.getRect();
            r.grow((int) -(r.width * TEXT_SHRINK), (int) -(r.height * TEXT_SHRINK));
        }
        return recs;
    }

    private void drawFill(Graphics2D g) {
    }

    private void drawGrid(Graphics2D g) {
        g.setStroke(new BasicStroke(STROKE_WIDTH));
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        int xInc = (WIDTH - STROKE_WIDTH) / COLS;
        int yInc = (HEIGHT - STROKE_WIDTH) / ROWS;

        // vertical lines
        int x1 = STROKE_WIDTH / 2, y1 = STROKE_WIDTH / 2, x2;
        for (int j = 0; j <= COLS; j++) {
            g.draw(new Line2D.Double(x1, y1, x1, HEIGHT));
            x1 += xInc;
        }
        // horizontal lines
        x1 = STROKE_WIDTH / 2;
        x2 = WIDTH - STROKE_WIDTH / 2;
        for (int j = 0; j <= ROWS; j++) {
            g.draw(new Line2D.Double(x1, y1, x2, y1));
            y1 += yInc;
        }
    }

    private static class TextRec {
        private final String text;
        private final Rectangle rect;

        private TextRec(String text, Rectangle rect) {
            this.text = text;
            this.rect = rect;
        }

        public String getText() {
            return text;
        }

        public Rectangle getRect() {
            return rect;
        }
    }
}
