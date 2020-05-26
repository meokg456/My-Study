package mystudy.Components;

import javax.swing.*;
import javax.swing.border.*;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;

import java.awt.*;

public class MyPasswordField extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JPasswordField passwordTextField;

    public MyPasswordField() {
        setOpaque(false);
        RoundedBorder line = new RoundedBorder(Color.WHITE, 1, true, 30);
        TitledBorder passwordBorder = BorderFactory.createTitledBorder(line, "Password");
        passwordTextField = new JPasswordField();
        passwordBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        passwordBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(passwordBorder, new EmptyBorder(5, 10, 12, 10)));
        setLayout(new BorderLayout());
        passwordTextField = new JPasswordField();
        passwordTextField.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 28));
        add(passwordTextField, BorderLayout.CENTER);
    }

    public String getPassword() {
        return String.copyValueOf(passwordTextField.getPassword());
    }

}