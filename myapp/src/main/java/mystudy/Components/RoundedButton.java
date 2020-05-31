package mystudy.Components;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;
import java.awt.*;
import java.awt.event.MouseEvent;

public class RoundedButton extends JPanel implements MouseInputListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int radius;

    public RoundedButton(String text, int radius, int fontSize) {
        this.radius = radius;
        setLayout(new BorderLayout());
        JLabel buttonLabel = new JLabel(text);
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(buttonLabel, BorderLayout.CENTER);
        buttonLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, fontSize));
        setBackground(Colors.getSecondary());
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(radius, radius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Component parent = getParent();
        if (parent != null)
            graphics.setColor(parent.getBackground());
        graphics.fillRect(0, 0, width, height); // paint background
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height); // paint background

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled == false) {
            setBackground(Colors.getAccentColor());
            removeMouseListener(this);
        } else {
            setBackground(Colors.getSecondary());
            addMouseListener(this);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setBackground(Colors.getSecondary());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setBackground(Colors.getSecondary().darker());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(Colors.getSecondary());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(Colors.getSecondary().brighter());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}