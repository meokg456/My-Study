package mystudy.Components;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int radius;

    public CardPanel(int radius) {
        this.radius = radius;
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

        graphics.setColor(parent.getBackground());
        graphics.fillRect(0, 0, width, height); // paint background
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height); // paint background

    }
}