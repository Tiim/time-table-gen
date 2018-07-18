package ch.scbirs.timetablegen.gui;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;

public class FileComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        if (value == null) {
            value = "";
        }
        return super.getListCellRendererComponent(list,
                FilenameUtils.getName(value.toString()), index, isSelected, cellHasFocus);
    }
}
