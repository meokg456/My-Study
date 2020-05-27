package mystudy.Fragment;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.hibernate.Session;
import org.hibernate.Transaction;

import mystudy.Colors.Colors;
import mystudy.Components.MyPasswordField;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.Routes.Routes;
import mystudy.User.User;
import mystudy.User.UserService;

public class AccountFragment extends JPanel {

    private User user = UserService.getInstance().getLoggedUser();

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AccountFragment() {
        buildProfileFragment();
    }

    private void buildProfileFragment() {
        JPanel accountPanel = this;
        setBackground(Colors.getBackground());
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Hello");
        label.setPreferredSize(new Dimension(100, 50));
        label.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        label.setForeground(Color.white);
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                user.getName());
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 50),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));
        add(label, BorderLayout.CENTER);
        label.setBackground(Colors.getAccentColor());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        bottomPanel.setBackground(Colors.getBackground());
        RoundedButton logoutButton = new RoundedButton("Logout", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                UserService.getInstance().setLoggedUser(null);
                Routes.getInstance().route("Login");
            }
        };
        logoutButton.setPreferredSize(new Dimension(200, 50));
        RoundedButton changePasswordButton = new RoundedButton("Change password", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                accountPanel.removeAll();
                buildChangePasswordFragment();
                accountPanel.validate();
                accountPanel.repaint();
            }
        };
        changePasswordButton.setPreferredSize(new Dimension(250, 50));
        bottomPanel.add(changePasswordButton);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.PAGE_END);
    }

    protected void buildChangePasswordFragment() {
        JPanel changePasswordPanel = this;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                "Change Password");
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 500, 250, 500),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));
        MyPasswordField oldPasswordField = new MyPasswordField("Old password", Colors.getTextColor(), 24);
        oldPasswordField.setMaximumSize(new Dimension(530, 100));
        add(Box.createVerticalStrut(40));
        add(oldPasswordField);
        MyPasswordField newPasswordField = new MyPasswordField("New password", Colors.getTextColor(), 24);
        newPasswordField.setMaximumSize(new Dimension(530, 100));
        add(Box.createVerticalStrut(10));
        add(newPasswordField);
        MyPasswordField confirmNewPasswordField = new MyPasswordField("Confirm new password", Colors.getTextColor(),
                24);
        confirmNewPasswordField.setMaximumSize(new Dimension(530, 100));
        add(Box.createVerticalStrut(10));
        add(confirmNewPasswordField);
        add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        bottomPanel.setBackground(getBackground());
        RoundedButton backButton = new RoundedButton("Back", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                changePasswordPanel.removeAll();
                buildProfileFragment();
                changePasswordPanel.validate();
                changePasswordPanel.repaint();
            }
        };
        RoundedButton changePasswordButton = new RoundedButton("Change password", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!user.getPassword().equals(oldPasswordField.getPassword())) {
                    JOptionPane.showMessageDialog(null, "Old password is not correct!", "Invalid",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newPasswordField.getPassword().equals(confirmNewPasswordField.getPassword())) {
                    JOptionPane.showMessageDialog(null, "New password and confirm new password does not match",
                            "Invalid", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    user.setPassword(newPasswordField.getPassword());
                    Transaction transaction;

                    Session session = DatabaseService.getInstance().getSession();
                    transaction = session.beginTransaction();
                    session.update(user);
                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Password has been changed!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    changePasswordPanel.removeAll();
                    buildProfileFragment();
                    changePasswordPanel.validate();
                    changePasswordPanel.repaint();
                }

            }
        };
        backButton.setPreferredSize(new Dimension(175, 50));
        changePasswordButton.setPreferredSize(new Dimension(250, 50));
        bottomPanel.add(backButton);
        bottomPanel.add(changePasswordButton);
        add(bottomPanel);
    }
}