package ch.scbirs.timetablegen.gui;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileComboBoxModel implements ComboBoxModel<File> {

    private int selected = -1;
    private final List<File> files = new ArrayList<>();

    private final List<ListDataListener> listeners = new ArrayList<>();

    @Override
    public void setSelectedItem(Object anItem) {
        assert anItem instanceof File;
        selected = files.indexOf(anItem);
        if (selected == -1) {
            files.add((File) anItem);
            selected = files.size() - 1;
        }
        listeners.forEach(l ->
                l.contentsChanged(
                        new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,0, files.size())
                )
        );
    }

    public void setSelectedItem(int i) {
        if (i >= 0 && i < files.size()) {
            selected = i;
        }
        listeners.forEach(l ->
                l.contentsChanged(
                        new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,0, files.size())
                )
        );
    }

    @Override
    public Object getSelectedItem() {
        if (selected == -1) {
            return null;
        }
        return files.get(selected);
    }

    @Override
    public int getSize() {
        return files.size();
    }

    @Override
    public File getElementAt(int index) {
        return files.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void set(File[] f) {
        selected = -1;
        files.clear();
        files.addAll(Arrays.asList(f));
        listeners.forEach(l ->
                l.contentsChanged(
                        new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,0, files.size())
                )
        );
    }

    public Stream<File> stream() {
        return files.stream();
    }
}
