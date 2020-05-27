package mystudy.Components;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;

import java.awt.*;
import java.awt.event.*;

public class IconButton extends JPanel implements MouseInputListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Color oldBackground;

    public Color getOldBackground() {
        return oldBackground;
    }

    public void setOldBackground(Color oldBackground) {
        this.oldBackground = oldBackground;
    }

    public IconButton(ImageIcon icon, String title) {
        setBackground(Colors.getPrimary());
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel leading = new JLabel(icon);

        JLabel label = new JLabel(title);
        label.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        label.setForeground(Colors.getTextColor());
        add(Box.createHorizontalStrut(50));
        if (leading != null) {
            add(leading);
        }
        add(Box.createHorizontalStrut(20));
        add(label);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        setBackground(Colors.getSecondary().darker());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setOldBackground(Colors.getSecondary());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setOldBackground(getBackground());
        setBackground(Colors.getSecondary().brighter());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(oldBackground);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}