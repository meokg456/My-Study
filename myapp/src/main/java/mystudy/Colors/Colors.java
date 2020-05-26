package mystudy.Colors;

import java.awt.Color;

public class Colors {

    private static Color background = new Color(0x071e3d);

    private static Color secondary = new Color(0x21e6c1);

    private static Color primary = new Color(0x1f4287);

    private static Color textColor = new Color(0xFFFFFF);

    private static Color accentColor = new Color(0x278ea5);

    public static Color getBackground() {
        return background;
    }

    public static Color getAccentColor() {
        return accentColor;
    }

    public static void setAccentColor(Color accentColor) {
        Colors.accentColor = accentColor;
    }

    public static Color getTextColor() {
        return textColor;
    }

    public static void setTextColor(Color textColor) {
        Colors.textColor = textColor;
    }

    public static Color getPrimary() {
        return primary;
    }

    public static void setPrimary(Color primary) {
        Colors.primary = primary;
    }

    public static Color getSecondary() {
        return secondary;
    }

    public static void setSecondary(Color secondary) {
        Colors.secondary = secondary;
    }

    public static void setBackground(Color background) {
        Colors.background = background;
    }

}