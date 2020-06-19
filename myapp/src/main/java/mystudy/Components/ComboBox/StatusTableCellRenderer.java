package mystudy.Components.ComboBox;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import mystudy.Colors.Colors;
import mystudy.Enum.RequestStatus;
import mystudy.Fonts.Fonts;

public class StatusTableCellRenderer extends JComboBox<RequestStatus> implements TableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public StatusTableCellRenderer(RequestStatus[] statusComboBox) {
        super(statusComboBox);
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        setForeground(Colors.getTextColor());
        setBackground(Colors.getPrimary());
        setRenderer(renderer);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelectedItem(value);
        return this;
    }

}