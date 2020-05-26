package mystudy.Screen;

import javax.swing.*;

import javax.swing.event.*;

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

import java.awt.*;
import java.awt.event.MouseEvent;

public class LoginScreen {

    public static JPanel build() {
        JPanel screenPanel = new JPanel();
        JPanel panel = new JPanel();
        screenPanel.setLayout(new BoxLayout(screenPanel, BoxLayout.PAGE_AXIS));
        screenPanel.add(Box.createRigidArea(
                new Dimension(0, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 800) / 2)));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        screenPanel.setBackground(Colors.getBackground());
        panel.setBackground(Colors.getBackground());

        RoundedButton button = new RoundedButton("Login", 30, 24);
        button.setMaximumSize(new Dimension(260, 50));

        JPanel login = new CardPanel(30);

        JLabel label = new JLabel("Login");
        label.setForeground(Colors.getTextColor());
        label.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 64));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        MyTextField username = new MyTextField("Username");
        username.setMaximumSize(new Dimension(530, 120));

        MyPasswordField password = new MyPasswordField();

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
        login.add(button);

        panel.add(login);
        screenPanel.add(panel);
        button.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(Colors.getSecondary());
                Session session = HibernateUtil.getSessionFactory().openSession();
                User user = (User) session.get(User.class, username.getText());
                System.out.println("Login in with " + user.getUsername());
                if (user.getPassword().equals(password.getPassword())) {
                    System.out.println("Password is correct");
                    Routes.getInstance().route("Dashboard");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Colors.getAccentColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Colors.getSecondary());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(Colors.getSecondary().getRGB() + 50));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        return screenPanel;
    }

}