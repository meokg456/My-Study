package mystudy.Fonts;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Fonts {

    private static Font font;
    static {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                    Fonts.class.getClassLoader().getResourceAsStream("Fonts/Lora/Lora-VariableFont_wght.ttf")));

            font = new Font("Lora Regular", Font.PLAIN, 20);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Font getFont() {
        return font;
    }

}