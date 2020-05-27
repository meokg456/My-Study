package mystudy.Components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;

public class MyTextField extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JTextField textField;

    public MyTextField(String title, Color color, int fontSize) {
        setOpaque(false);
        RoundedBorder line = new RoundedBorder(color, 1, true, 30);
        TitledBorder border = BorderFactory.createTitledBorder(line, title);
        border.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, fontSize));
        border.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(border, new EmptyBorder(5, 10, 12, 10)));
        setLayout(new BorderLayout());
        textField = new JTextField();
        textField.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 28));
        add(textField, BorderLayout.CENTER);
    }

    public String getText() {
        return textField.getText();
    }

}