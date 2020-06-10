package mystudy.Components.ComboBox;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import mystudy.Colors.Colors;

import java.awt.*;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        list.setSelectionBackground(Colors.getPrimary().darker());
        list.setSelectionForeground(Colors.getTextColor());
        list.setBackground(Colors.getPrimary());
        list.setForeground(Colors.getTextColor());
        list.setSelectionForeground(Colors.getTextColor());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value != null)
            setText(value.toString());
        else
            setText("");
        return this;
    }

}