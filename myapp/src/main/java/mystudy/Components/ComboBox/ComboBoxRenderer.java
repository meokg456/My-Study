package mystudy.Components.ComboBox;

import javax.swing.*;

import mystudy.Colors.Colors;
import mystudy.POJOs.Class;

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
        // list.setBackground(Colors.getPrimary().darker());
        // list.setForeground(Colors.getTextColor());
        list.setSelectionBackground(Colors.getPrimary().darker());
        list.setSelectionForeground(Colors.getTextColor());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        Class myClass = (Class) value;
        if (myClass != null)
            setText(myClass.getClassName());

        return this;
    }
}