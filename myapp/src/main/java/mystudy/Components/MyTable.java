package mystudy.Components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;

public class MyTable extends JTable implements MouseInputListener {

    private int rollOverRowIndex = -1;

    public MyTable(TableModel model) {
        setModel(model);
        setSelectionBackground(Colors.getPrimary().darker());
        setBackground(Colors.getPrimary());
        addMouseMotionListener(this);
        addMouseListener(this);

        setForeground(Colors.getTextColor());
        setSelectionForeground(Colors.getTextColor());
        JTableHeader header = getTableHeader();
        header.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        header.setBackground(Colors.getBackground());
        header.setForeground(Colors.getTextColor());
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if ((isRowSelected(row) || (row == rollOverRowIndex))) {
            c.setForeground(getSelectionForeground());
            c.setBackground(getSelectionBackground());

        } else {
            c.setForeground(getForeground());
            c.setBackground(getBackground());

        }
        return c;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        rollOverRowIndex = -1;
        repaint();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int row = rowAtPoint(e.getPoint());
        int column = columnAtPoint(e.getPoint());
        if (row != rollOverRowIndex) {
            rollOverRowIndex = row;
            repaint();
        }
        editCellAt(row, column);
    }

}