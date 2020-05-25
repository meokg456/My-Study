package mystudy.Screen;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import mystudy.Colors.Colors;
import mystudy.Fonts.Fonts;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LoginScreen {
    static Colors colors = Colors.getInstance();

    public static JPanel build() {
        JPanel screenPanel = new JPanel();
        JPanel panel = new JPanel();
        screenPanel.setLayout(new BoxLayout(screenPanel, BoxLayout.PAGE_AXIS));
        System.out.println();
        screenPanel.add(Box.createRigidArea(
                new Dimension(0, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 800) / 2)));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        screenPanel.setBackground(colors.getBackground());

        JPanel button = new JPanel();
        button.setLayout(new BorderLayout());
        JLabel buttonLabel = new JLabel("Login");
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        button.add(buttonLabel, BorderLayout.CENTER);
        buttonLabel.setFont(new Font(Fonts.getInstance().getFont().getName(), Font.PLAIN, 18));
        button.setBackground(colors.getSecondary());
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(260, 50));
        button.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(colors.getSecondary());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(colors.getAccentColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(colors.getSecondary());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(colors.getSecondary().getRGB() + 50));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        JPanel login = new JPanel();

        JLabel label = new JLabel("Login");
        label.setForeground(colors.getTextColor());
        label.setFont(new Font(Fonts.getInstance().getFont().getName(), Font.PLAIN, 64));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel username = new JPanel();
        username.setOpaque(false);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder usernameBorder = BorderFactory.createTitledBorder(blackline, "Username");
        usernameBorder.setTitleFont(new Font(Fonts.getInstance().getFont().getName(), Font.PLAIN, 36));
        usernameBorder.setTitleColor(colors.getTextColor());
        username.setBorder(new CompoundBorder(usernameBorder, new EmptyBorder(5, 5, 5, 5)));
        username.setLayout(new BorderLayout());
        JTextField usernameTextField = new JTextField();
        usernameTextField.setFont(new Font(Fonts.getInstance().getFont().getName(), Font.PLAIN, 36));
        username.add(usernameTextField, BorderLayout.CENTER);
        username.setMaximumSize(new Dimension(530, 120));

        JPanel password = new JPanel();
        password.setOpaque(false);

        TitledBorder passwordBorder = BorderFactory.createTitledBorder(blackline, "Password");
        passwordBorder.setTitleFont(new Font(Fonts.getInstance().getFont().getName(), Font.PLAIN, 36));
        passwordBorder.setTitleColor(colors.getTextColor());
        password.setBorder(new CompoundBorder(passwordBorder, new EmptyBorder(5, 5, 5, 5)));
        password.setLayout(new BorderLayout());
        JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setFont(new Font(Fonts.getInstance().getFont().getName(), Font.PLAIN, 36));
        password.add(passwordTextField, BorderLayout.CENTER);
        password.setMaximumSize(new Dimension(530, 120));

        login.setBackground(colors.getPrimary());
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
        return screenPanel;
    }

}