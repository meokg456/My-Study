package mystudy.Components;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.border.*;

public class RoundedBorder extends LineBorder {

    private float radius;

    public RoundedBorder(Color color, int thickness, boolean roundedCorners, float radius) {
        super(color, thickness, roundedCorners);
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if ((this.thickness > 0) && (g instanceof Graphics2D)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color oldColor = g2d.getColor();
            g2d.setColor(this.lineColor);

            Shape outer;
            Shape inner;

            int offs = this.thickness;
            int size = offs + offs;
            if (this.roundedCorners) {
                float arc = radius;
                outer = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
                inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, arc, arc);
            } else {
                outer = new Rectangle2D.Float(x, y, width, height);
                inner = new Rectangle2D.Float(x + offs, y + offs, width - size, height - size);
            }
            Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
            path.append(outer, false);
            path.append(inner, false);
            g2d.fill(path);
            g2d.setColor(oldColor);
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}