package ch.scbirs.timetablegen.util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class ChangeListener {
    public static void add (JTextField d, Consumer<String> c) {
        d.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                c.accept(d.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                c.accept(d.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                c.accept(d.getText());
            }
        });
    }
}
