package mystudy.Table;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import mystudy.Components.RoundedButton;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    RoundedButton button;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ButtonEditor() {
        button = new RoundedButton("Reset password", 50, 24);
    }

    @Override
    public Object getCellEditorValue() {
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

}