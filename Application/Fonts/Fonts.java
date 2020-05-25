package Application.Fonts;

import java.awt.*;

public class Fonts {
    private static Fonts instance;

    public static Fonts getInstance() {
        if (instance == null) {
            instance = new Fonts();
        }
        return instance;
    }

    private Font font = new Font("Book Antiqua", Font.PLAIN, 20);

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}