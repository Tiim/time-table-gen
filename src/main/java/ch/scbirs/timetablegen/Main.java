package ch.scbirs.timetablegen;

import ch.scbirs.timetablegen.gui.MainWindow;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setUIFont();
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }

    private static void setUIFont()
    {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
            {
                FontUIResource font = (FontUIResource) value;
                UIManager.put(key, new FontUIResource(font.getName(), font.getStyle(), font.getSize() + 5));
            }
        }
    }
}
