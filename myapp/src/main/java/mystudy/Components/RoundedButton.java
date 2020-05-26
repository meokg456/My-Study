package mystudy.Components;

import javax.swing.*;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;
import java.awt.*;

public class RoundedButton extends JPanel {

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
        setAlignmentX(Component.LEFT_ALIGNMENT);
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