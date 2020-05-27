package mystudy.Components;

import javax.swing.*;
import javax.swing.border.*;

import mystudy.Fonts.Fonts;

import java.awt.*;

public class MyPasswordField extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JPasswordField textField;

    public MyPasswordField(String title, Color titleColor, int fontSize) {
        setOpaque(false);
        RoundedBorder line = new RoundedBorder(titleColor, 1, true, 30);
        TitledBorder border = BorderFactory.createTitledBorder(line, title);
        textField = new JPasswordField();
        border.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, fontSize));
        border.setTitleColor(titleColor);
        setBorder(new CompoundBorder(border, new EmptyBorder(5, 10, 12, 10)));
        setLayout(new BorderLayout());
        textField = new JPasswordField();
        textField.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 28));
        add(textField, BorderLayout.CENTER);
    }

    public String getPassword() {
        return String.copyValueOf(textField.getPassword());
    }

    public void setPassword(String password) {
        textField.setText(password);
    }
}