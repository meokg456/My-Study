package mystudy.Fragment;

import java.util.List;
import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mystudy.Colors.Colors;
import mystudy.Components.CardPanel;
import mystudy.Components.MyTable;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.*;

import mystudy.Table.UserAccountListModel;

public class UsersFragment extends JPanel {

    private int selectedIndex = -1;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UsersFragment() {
        Session session = DatabaseService.getInstance().getSession();
        List<User> users = session.createQuery("from User", User.class).list();
        UserAccountListModel userAccountListModel = new UserAccountListModel(users);
        setBackground(Colors.getBackground());
        setLayout(new BorderLayout());
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Users");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 50),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));

        RoundedButton resetButton = new RoundedButton("Reset Password", 50, 24) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                User user = users.get(selectedIndex);
                int result = JOptionPane.showConfirmDialog(null, "Your password will be reset", "Are you sure?",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    user.setPassword(user.getUsername());
                    Transaction transaction = null;
                    try {
                        transaction = session.beginTransaction();
                        session.update(user);
                        transaction.commit();
                        userAccountListModel.fireTableDataChanged();
                        setEnabled(false);
                    } catch (HibernateException ex) {
                        ex.printStackTrace();
                        transaction.rollback();
                    }
                }
            }
        };
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resetButton.setEnabled(false);
        resetButton.setPreferredSize(new Dimension(200, 50));
        topPanel.add(resetButton);

        add(topPanel, BorderLayout.PAGE_START);
        topPanel.setBackground(topPanel.getParent().getBackground());
        // Hiển thị thông tin giáo vụ

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());

        centerJPanel.add(Box.createVerticalStrut(50));

        // Users account table
        MyTable accountTable = new MyTable(userAccountListModel) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                resetButton.setEnabled(true);
                selectedIndex = getSelectedRow();
            }
        };
        accountTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        accountTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setBorder(new EmptyBorder(10, 10, 10, 10));
        accountTable.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 24));
        accountTable.getColumn("Username").setCellRenderer(textRenderer);
        accountTable.getColumn("Password").setCellRenderer(textRenderer);
        accountTable.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(accountTable);
        centerJPanel.add(scrollPane);
    }
}