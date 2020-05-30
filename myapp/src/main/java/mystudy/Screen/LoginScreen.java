package mystudy.Screen;

import javax.swing.*;

import org.hibernate.Session;

import mystudy.Colors.Colors;
import mystudy.Components.RoundedButton;
import mystudy.Connector.DatabaseService;
import mystudy.Components.CardPanel;
import mystudy.Components.MyPasswordField;
import mystudy.Components.MyTextField;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.User;
import mystudy.Routes.Routes;
import mystudy.User.UserService;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LoginScreen implements Screen {

    public JPanel build() {
        Session session = DatabaseService.getInstance().getSession();
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());

        screenPanel.setBackground(Colors.getBackground());

        JPanel login = new CardPanel(30);

        JLabel label = new JLabel("Login");
        label.setForeground(Colors.getTextColor());
        label.setFont(new Font(Fonts.getFont().getName(), Font.BOLD, 64));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        MyTextField usernameTextField = new MyTextField("Username", Colors.getTextColor(), 36);
        usernameTextField.setMaximumSize(new Dimension(530, 120));

        MyPasswordField passwordTextField = new MyPasswordField("Password", Colors.getTextColor(), 36);

        passwordTextField.setMaximumSize(new Dimension(530, 120));

        login.setBackground(Colors.getPrimary());
        login.setPreferredSize(new Dimension(600, 800));
        login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
        login.add(Box.createRigidArea(new Dimension(0, 100)));
        login.add(label);
        login.add(Box.createRigidArea(new Dimension(0, 75)));
        login.add(usernameTextField);
        login.add(passwordTextField);
        login.add(Box.createRigidArea(new Dimension(0, 50)));

        screenPanel.add(login);
        RoundedButton button = new RoundedButton("Login", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setBackground(Colors.getSecondary());
                String username = usernameTextField.getText();
                String password = passwordTextField.getPassword();
                if (username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Username can not be empty!", "Empty username",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Password can not be empty!", "Empty password",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                User user = (User) session.get(User.class, username);
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "Your username is not existed!", "Invalid username",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (user.getPassword().equals(password)) {
                    System.out.println("Login in with " + user.getUsername());
                    UserService.getInstance().setLoggedUser(user);
                    Routes.getInstance().route("Dashboard");
                    passwordTextField.setPassword("");
                    session.clear();

                } else {
                    JOptionPane.showMessageDialog(null, "Your password is wrong!", "Invalid password",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        button.setMaximumSize(new Dimension(260, 50));
        login.add(button);
        return screenPanel;
    }

}