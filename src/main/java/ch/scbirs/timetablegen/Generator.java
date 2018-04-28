package ch.scbirs.timetablegen;

import ch.scbirs.timetablegen.util.Lang;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    private static final int WIDTH_MULTIPLIER = 60;
    private static final int HEIGHT_MULTIPLIER = 100;

    private static final Color FILL_COLOR = new Color(0x005fab);
    private static final int STROKE_WIDTH = 7;
    private static final String[] DAYS = Lang.translate("day.All").split(",");

    private static final int WEEK_DAYS = 5;
    private static final int DAY_WIDTH = 2, UNIT_WIDTH = 4;

    private static final Font FONT = new Font("Arial", Font.PLAIN, 80);
    private static final double TEXT_SHRINK = 0.1;


    private final int startWeekday;
    private final int lengthWeekday;
    private final int startWeekend;
    private final int lengthWeekend;
    private final int maxLength;

    private final int cols, rows;
    private final int width, height;

    private final Model model;

    public Generator(Model model) {

        this.model = model;

        int minWeekday = model.getWeekdayMinHour() - 1;
        int maxWeekday = model.getWeekdayMaxHour() + 1;
        startWeekday = minWeekday;
        lengthWeekday = maxWeekday - minWeekday;
        int minWeekend = model.getWeekendMinHour() - 1;
        int maxWeekend = model.getWeekendMaxHour() + 1;
        startWeekend = minWeekend;
        lengthWeekend = maxWeekend - minWeekend;
        maxLength = Math.max(lengthWeekday, lengthWeekend);
        cols = (DAY_WIDTH + maxLength) * UNIT_WIDTH;
        rows = DAYS.length + 3;
        width = WIDTH_MULTIPLIER * cols + STROKE_WIDTH;
        height = HEIGHT_MULTIPLIER * rows + STROKE_WIDTH;

    }

    public BufferedImage generate() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHints(rh);
        g.setFont(FONT);
        g.setPaint(Color.white);
        g.fill(new Rectangle(0, 0, width, height));
        g.setPaint(Color.black);
        drawGrid(g);
        drawGridCover(g);
        drawFill(g);
        drawText(g);
        return img;
    }

    private void drawGridCover(Graphics2D g) {
        g.setColor(Color.WHITE);

        fillCell(g, 0, 0, 8, 1, true, false, true, false);
        fillCell(g, 0, WEEK_DAYS + 2, 8, 1, true, false, true, false);
        // Remove lines from headers
        for (int i = DAY_WIDTH; i < (cols / UNIT_WIDTH); i++) {
            fillCell(g, UNIT_WIDTH * i, 0, UNIT_WIDTH, 1, false, false, false, false);
            fillCell(g, UNIT_WIDTH * i, WEEK_DAYS + 2, UNIT_WIDTH, 1, false, false, false, false);
        }
        // Remove lines from Day Slot
        for (int i = 0; i < rows; i++) {
            fillCell(g, 0, i, DAY_WIDTH * UNIT_WIDTH, 1, false, false, false, false);
        }
        // Remove lines between weekdays and weekend
        fillCell(g, 0, WEEK_DAYS + 1, cols, 1, false, true, true, false);
    }

    private void fillCell(Graphics2D g, int x, int y, int w, int h, boolean coverTop, boolean coverRight, boolean coverLeft, boolean coverBottom) {
        Rectangle rect = cellRect(x, y, w, h, coverTop, coverRight, coverLeft, coverBottom);
        g.fill(rect);
    }

    private Rectangle cellRect(int x, int y, int w, int h, boolean coverTop, boolean coverRight, boolean coverLeft, boolean coverBottom) {
        int xInc = (width - STROKE_WIDTH) / cols;
        int yInc = (height - STROKE_WIDTH) / rows;

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
        g.setColor(Color.BLACK);
        List<TextRec> recs = getTextRecs();
        g.setFont(scaleFont(recs, g));

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
        // day names
        for (int i = 0; i < DAYS.length; i++) {
            int j = i < WEEK_DAYS ? i + 1 : i + 3;
            recs.add(new TextRec(
                    DAYS[i],
                    cellRect(0, j, DAY_WIDTH * UNIT_WIDTH, 1)
            ));
        }
        // Times weekday
        for (int i = 0; i < maxLength; i++) {
            recs.add(new TextRec(
                    String.format("%d:00", (startWeekday + i) % 24),
                    cellRect((UNIT_WIDTH * DAY_WIDTH) + (UNIT_WIDTH * i), 0, UNIT_WIDTH, 1)
            ));
        }
        //Times weekend
        for (int i = 0; i < maxLength; i++) {
            recs.add(new TextRec(
                    String.format("%d:00", (startWeekend + i) % 24),
                    cellRect((UNIT_WIDTH * DAY_WIDTH) + (UNIT_WIDTH * i), WEEK_DAYS + 2, UNIT_WIDTH, 1)
            ));
        }
        // Shrink the rects
        for (TextRec rec : recs) {
            Rectangle r = rec.getRect();
            r.grow((int) -(r.width * TEXT_SHRINK), (int) -(r.height * TEXT_SHRINK));
        }
        return recs;
    }

    private void drawFill(Graphics2D g) {
        g.setColor(FILL_COLOR);
        for (int i = 0; i < DAYS.length; i++) {
            int row = i < 5 ? i + 1 : i + 3;
            Model.TimeRange range = model.getRange(i);
            if (!range.isEnabled()) {
                continue;
            }
            LocalTime start = range.getStart();
            LocalTime end = range.getEnd();
            int minTime;
            if (i < 5) {
                minTime = startWeekday;
            } else {
                minTime = startWeekend;
            }
            long mins = ChronoUnit.MINUTES.between(start, end);
            long offset = ChronoUnit.MINUTES.between(LocalTime.of(minTime, 0), start) / 15;
            for (int j = 0; j < mins / 15; j++) {
                fillCell(g, (int) offset + (DAY_WIDTH * UNIT_WIDTH) + j, row, 1, 1, false, false, false, false);
            }
        }
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(STROKE_WIDTH));
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        int xInc = (width - STROKE_WIDTH) / cols;
        int yInc = (height - STROKE_WIDTH) / rows;

        // vertical lines
        int x1 = STROKE_WIDTH / 2, y1 = STROKE_WIDTH / 2, x2;
        for (int j = 0; j <= cols; j++) {
            g.draw(new Line2D.Double(x1, y1, x1, height));
            x1 += xInc;
        }
        // horizontal lines
        x1 = STROKE_WIDTH / 2;
        x2 = width - STROKE_WIDTH / 2;
        for (int j = 0; j <= rows; j++) {
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
