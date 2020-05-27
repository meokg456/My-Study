package mystudy.Screen;

import javax.swing.*;

import org.hibernate.Session;

import mystudy.Colors.Colors;
import mystudy.Components.RoundedButton;
import mystudy.Components.CardPanel;
import mystudy.Components.MyPasswordField;
import mystudy.Components.MyTextField;
import mystudy.Fonts.Fonts;
import mystudy.Hibernate.HibernateUtil;
import mystudy.Routes.Routes;
import mystudy.User.User;
import mystudy.User.UserService;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LoginScreen implements Screen {

    public JPanel build() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());

        screenPanel.setBackground(Colors.getBackground());

        JPanel login = new CardPanel(30);

        JLabel label = new JLabel("Login");
        label.setForeground(Colors.getTextColor());
        label.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 64));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        MyTextField username = new MyTextField("Username", Colors.getTextColor(), 36);
        username.setMaximumSize(new Dimension(530, 120));

        MyPasswordField password = new MyPasswordField("Password", Colors.getTextColor(), 36);

        password.setMaximumSize(new Dimension(530, 120));

        login.setBackground(Colors.getPrimary());
        login.setPreferredSize(new Dimension(600, 800));
        login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
        login.add(Box.createRigidArea(new Dimension(0, 100)));
        login.add(label);
        login.add(Box.createRigidArea(new Dimension(0, 75)));
        login.add(username);
        login.add(password);
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

                User user = (User) session.get(User.class, username.getText());
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "Your username is not existed", "Invalid username",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (user.getPassword().equals(password.getPassword())) {
                    System.out.println("Login in with " + user.getUsername());
                    UserService.getInstance().setLoggedUser(user);
                    Routes.getInstance().route("Dashboard");
                    password.setPassword("");

                } else {
                    JOptionPane.showMessageDialog(null, "Your password is wrong", "Invalid password",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        button.setMaximumSize(new Dimension(260, 50));
        login.add(button);
        return screenPanel;
    }

}