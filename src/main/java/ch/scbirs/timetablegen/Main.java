package ch.scbirs.timetablegen;

import ch.scbirs.timetablegen.gui.MainWindow;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setUIFont();
        MainWindow window = new MainWindow();
        window.setVisible(true);
        window.setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);
    }

    private static void setUIFont() {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                FontUIResource font = (FontUIResource) value;
                UIManager.put(key, new FontUIResource(font.getName(), font.getStyle(), font.getSize() + 5));
            }
        }
    }
}
