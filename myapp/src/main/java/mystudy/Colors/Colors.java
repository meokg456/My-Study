package mystudy.Colors;

import java.awt.Color;

public class Colors {
    private static Colors instance;

    private Color background = new Color(0x071e3d);

    private Color secondary = new Color(0x21e6c1);

    private Color primary = new Color(0x1f4287);

    private Color textColor = new Color(0xFFFFFF);

    private Color accentColor = new Color(0x278ea5);

    public static Colors getInstance() {
        if (instance == null)
            instance = new Colors();
        return instance;
    }

    public Color getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getPrimary() {
        return primary;
    }

    public void setPrimary(Color primary) {
        this.primary = primary;
    }

    public Color getSecondary() {
        return secondary;
    }

    public void setSecondary(Color secondary) {
        this.secondary = secondary;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

}