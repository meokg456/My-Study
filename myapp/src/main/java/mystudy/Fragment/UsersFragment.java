package mystudy.Fragment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mystudy.Colors.Colors;
import mystudy.Components.CardPanel;
import mystudy.Components.MyTable;
import mystudy.Components.RoundedBorder;
import mystudy.Components.RoundedButton;
import mystudy.Components.Table.UserAccountListModel;
import mystudy.Connector.DatabaseService;
import mystudy.Fonts.Fonts;
import mystudy.POJOs.User;

public class UsersFragment extends JPanel implements Fragment {

    private int selectedIndex = -1;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JPanel getPanel() {
        return this;
    }

    public UsersFragment() {
        setBackground(Colors.getBackground());

    }

    public void build() {
        removeAll();
        setLayout(new BorderLayout(0, 20));
        TitledBorder titledBorder = new TitledBorder(new RoundedBorder(Colors.getPrimary(), 2, true, 30), "Users");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(new Font(Fonts.getFont().getName(), Font.BOLD, 36));
        titledBorder.setTitleColor(Colors.getTextColor());
        setBorder(new CompoundBorder(new EmptyBorder(150, 100, 50, 100),
                (new CompoundBorder(titledBorder, new EmptyBorder(30, 30, 30, 30)))));
        Session session = DatabaseService.getInstance().getSession();
        List<User> users = session.createQuery("from User u order by u.username", User.class).list();
        session.clear();
        UserAccountListModel userAccountListModel = new UserAccountListModel(users);

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
                        session.clear();
                    } catch (HibernateException ex) {
                        ex.printStackTrace();
                        transaction.rollback();
                    }
                }
            }
        };
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottomPanel, BorderLayout.PAGE_END);
        resetButton.setEnabled(false);
        resetButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(resetButton);

        bottomPanel.setBackground(bottomPanel.getParent().getBackground());
        // Hiển thị thông tin giáo vụ

        CardPanel centerJPanel = new CardPanel(50);
        add(centerJPanel, BorderLayout.CENTER);
        centerJPanel.setLayout(new BoxLayout(centerJPanel, BoxLayout.Y_AXIS));
        centerJPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        centerJPanel.setBackground(Colors.getPrimary());

        // Hiển thị bảng account của tất cả mọi người
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