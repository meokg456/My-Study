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
    JTextField usernameTextField;

    public MyTextField(String title) {
        setOpaque(false);
        RoundedBorder line = new RoundedBorder(Color.white, 1, true, 30);
        TitledBorder usernameBorder = BorderFactory.createTitledBorder(line, title);
        usernameBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        usernameBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(usernameBorder, new EmptyBorder(5, 10, 12, 10)));
        setLayout(new BorderLayout());
        usernameTextField = new JTextField();
        usernameTextField.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 28));
        add(usernameTextField, BorderLayout.CENTER);
    }

    public String getText() {
        return usernameTextField.getText();
    }

}