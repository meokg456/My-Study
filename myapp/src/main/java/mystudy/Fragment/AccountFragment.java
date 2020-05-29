package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mystudy.Colors.Colors;
import mystudy.Components.CardPanel;
import mystudy.Components.MyPasswordField;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Connector.DatabaseService;
import mystudy.Enum.Permission;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.Student;
import mystudy.POJOs.User;
import mystudy.POJOs.Class;
import mystudy.Routes.Routes;
import mystudy.User.UserService;

public class AccountFragment extends JPanel implements Fragment {

    private User user = UserService.getInstance().getLoggedUser();

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JPanel getPanel() {
        return this;
    }

    public AccountFragment() {
        setBackground(Colors.getBackground());
        setLayout(new BorderLayout());
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Profile");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 50),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));
    }

    private JPanel buildStudentProfile(Student student) {

        // Hiển thị thông tin sinh viên
        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 10, 10));
        centerJPanel.setBackground(Colors.getPrimary());

        centerJPanel.add(Box.createVerticalStrut(50));
        // Username
        JLabel usernameJLabel = new JLabel();
        centerJPanel.add(usernameJLabel);
        usernameJLabel.setBackground(centerJPanel.getBackground());
        usernameJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        usernameJLabel.setText("Username: " + user.getUsername());
        usernameJLabel.setForeground(Colors.getTextColor());
        centerJPanel.add(Box.createVerticalStrut(20));
        // Mã số sinh viên
        JLabel studentIdJLabel = new JLabel();
        centerJPanel.add(studentIdJLabel);
        studentIdJLabel.setBackground(centerJPanel.getBackground());
        studentIdJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        studentIdJLabel.setText("Student ID: " + student.getStudentId());
        studentIdJLabel.setForeground(Colors.getTextColor());
        centerJPanel.add(Box.createVerticalStrut(20));
        // Họ và tên
        JLabel fullnameJLabel = new JLabel();
        centerJPanel.add(fullnameJLabel);
        fullnameJLabel.setBackground(centerJPanel.getBackground());
        fullnameJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        fullnameJLabel.setText("Full name: " + student.getName());
        fullnameJLabel.setForeground(Colors.getTextColor());
        centerJPanel.add(Box.createVerticalStrut(20));
        // Tên lớp
        JLabel classNameJLabel = new JLabel();
        centerJPanel.add(classNameJLabel);
        classNameJLabel.setBackground(centerJPanel.getBackground());
        classNameJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        classNameJLabel.setText("Class name: " + student.getClassName().getClassName());
        classNameJLabel.setForeground(Colors.getTextColor());
        centerJPanel.add(Box.createVerticalStrut(20));
        // Giới tính
        JLabel genderJLabel = new JLabel();
        centerJPanel.add(genderJLabel);
        genderJLabel.setBackground(centerJPanel.getBackground());
        genderJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        genderJLabel.setText("Gender: " + student.getGender());
        genderJLabel.setForeground(Colors.getTextColor());
        centerJPanel.add(Box.createVerticalStrut(20));
        // Số CMND
        JLabel personalIdJLabel = new JLabel();
        centerJPanel.add(personalIdJLabel);
        personalIdJLabel.setBackground(centerJPanel.getBackground());
        personalIdJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        personalIdJLabel.setText("Personal ID: " + student.getPersonalId());
        personalIdJLabel.setForeground(Colors.getTextColor());

        return centerJPanel;
    }

    public void build() {
        removeAll();
        Student student = new Student("1712368", "Nguyễn Hữu Dũng", "Nam", "123456789", new Class("17CTT3"));
        // Student student = user.getStudent();
        JPanel accountPanel = this;

        if (user.getPermission().equals(Permission.STUDENT)) {
            // Hiển thị thông tin sinh viên
            add(buildStudentProfile(student));
        } else {
            add(buildAdminProfile());
        }

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

    private JPanel buildAdminProfile() {
        // Hiển thị thông tin giáo vụ
        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 10, 10));
        centerJPanel.setBackground(Colors.getPrimary());

        centerJPanel.add(Box.createVerticalStrut(50));
        // Username
        JLabel usernameJLabel = new JLabel();
        centerJPanel.add(usernameJLabel);
        usernameJLabel.setBackground(centerJPanel.getBackground());
        usernameJLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        usernameJLabel.setText("Username: " + user.getUsername());
        usernameJLabel.setForeground(Colors.getTextColor());
        centerJPanel.add(Box.createVerticalStrut(20));

        return centerJPanel;
    }

    protected void buildChangePasswordFragment() {
        JPanel changePasswordPanel = this;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30),
                "Change Password");
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 490, 250, 490),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 50, 30)))));
        MyPasswordField oldPasswordField = new MyPasswordField("Old password", Colors.getTextColor(), 24);
        oldPasswordField.setMaximumSize(new Dimension(530, 100));
        CardPanel card = new CardPanel(50);
        add(card);
        card.setBackground(Colors.getPrimary());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.add(Box.createVerticalStrut(40));
        card.add(oldPasswordField);
        MyPasswordField newPasswordField = new MyPasswordField("New password", Colors.getTextColor(), 24);
        newPasswordField.setMaximumSize(new Dimension(530, 100));
        card.add(Box.createVerticalStrut(10));
        card.add(newPasswordField);
        MyPasswordField confirmNewPasswordField = new MyPasswordField("Confirm new password", Colors.getTextColor(),
                24);
        confirmNewPasswordField.setMaximumSize(new Dimension(530, 100));
        card.add(Box.createVerticalStrut(10));
        card.add(confirmNewPasswordField);
        card.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        card.add(Box.createVerticalStrut(20));
        card.add(bottomPanel);
        bottomPanel.setBackground(bottomPanel.getParent().getBackground());
        RoundedButton backButton = new RoundedButton("Back", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                changePasswordPanel.removeAll();
                build();
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

                    Session session = DatabaseService.getInstance().getSession();
                    Transaction transaction = null;
                    try {
                        transaction = session.beginTransaction();
                        session.update(user);
                        transaction.commit();
                    } catch (HibernateException ex) {
                        ex.printStackTrace();
                        transaction.rollback();
                    }
                    JOptionPane.showMessageDialog(null, "Password has been changed!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    changePasswordPanel.removeAll();
                    build();
                    changePasswordPanel.validate();
                    changePasswordPanel.repaint();
                }

            }
        };
        backButton.setPreferredSize(new Dimension(175, 50));
        changePasswordButton.setPreferredSize(new Dimension(250, 50));
        bottomPanel.add(backButton);
        bottomPanel.add(changePasswordButton);

    }
}