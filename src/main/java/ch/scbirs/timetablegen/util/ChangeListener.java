package ch.scbirs.timetablegen.util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class ChangeListener {

    public static DocumentListener add (JTextField d, Consumer<String> c) {
        DocumentListener listener = new DocumentListener() {
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
        };
        d.getDocument().addDocumentListener(listener);
        return listener;
    }

    public static void remove(JTextField d, DocumentListener dl) {
        d.getDocument().removeDocumentListener(dl);
    }
}
