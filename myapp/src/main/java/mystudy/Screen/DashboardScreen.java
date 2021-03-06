package mystudy.Screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MouseInputListener;

import mystudy.Colors.Colors;
import mystudy.Components.TabItem;
import mystudy.Enum.Permission;
import mystudy.Fonts.Fonts;
import mystudy.Fragment.AccountFragment;
import mystudy.Fragment.CoursesFragment;
import mystudy.Fragment.Fragment;
import mystudy.Fragment.ReexamineFragment;
import mystudy.Fragment.ResultFragment;
import mystudy.Fragment.StudentsFragment;
import mystudy.Fragment.TimeTableFragment;
import mystudy.Fragment.UsersFragment;
import mystudy.User.UserService;

public class DashboardScreen implements Screen, MouseInputListener {

    private TabItem selectedTabItem = null;
    private Map<JPanel, Fragment> tabItems = new LinkedHashMap<>();
    private JPanel pane = new JPanel();

    public JPanel build() {
        pane.removeAll();
        tabItems.clear();
        selectedTabItem = null;
        pane.setBackground(Colors.getBackground());
        pane.setLayout(new BorderLayout());
        // Tạo Menu điều hướng bên trái
        JPanel navigatePanel = new JPanel();
        navigatePanel.setLayout(new BoxLayout(navigatePanel, BoxLayout.Y_AXIS));
        navigatePanel.setBackground(Colors.getPrimary());
        navigatePanel.setPreferredSize(new Dimension(400, 0));
        navigatePanel.setBorder(new EmptyBorder(100, 0, 0, 0));

        JLabel dashboardLabel = new JLabel("Dashboard");
        dashboardLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 48));
        dashboardLabel.setForeground(Colors.getTextColor());
        Box dashboardBox = Box.createHorizontalBox();
        dashboardBox.add(dashboardLabel);
        navigatePanel.add(dashboardBox);
        navigatePanel.add(Box.createVerticalStrut(50));

        AccountFragment accountFragment = new AccountFragment();
        UsersFragment userFragment = new UsersFragment();
        StudentsFragment studentsFragment = new StudentsFragment();
        TimeTableFragment timeTableFragment = new TimeTableFragment();
        CoursesFragment coursesFragment = new CoursesFragment();
        ResultFragment resultFragment = new ResultFragment();
        ReexamineFragment reexamineFragment = new ReexamineFragment();
        // Tạo nút tab
        ClassLoader loader = this.getClass().getClassLoader();
        TabItem accountTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/user.png")), "Account");
        accountTabItem.addMouseListener(this);
        accountTabItem.setMaximumSize(new Dimension(390, 50));

        TabItem studentsTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/student.png")), "Student");
        studentsTabItem.addMouseListener(this);
        studentsTabItem.setMaximumSize(new Dimension(390, 50));

        TabItem usersTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/team.png")), "User");
        usersTabItem.addMouseListener(this);
        usersTabItem.setMaximumSize(new Dimension(390, 50));

        TabItem timeTableTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/calendar.png")), "Time table");
        timeTableTabItem.addMouseListener(this);
        timeTableTabItem.setMaximumSize(new Dimension(390, 50));

        TabItem courseTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/book.png")), "Course");
        courseTabItem.addMouseListener(this);
        courseTabItem.setMaximumSize(new Dimension(390, 50));

        TabItem resultTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/result.png")), "Result");
        resultTabItem.addMouseListener(this);
        resultTabItem.setMaximumSize(new Dimension(390, 50));

        TabItem reexamineTabItem = new TabItem(new ImageIcon(loader.getResource("Icons/reexamine.png")), "Reexamine");
        reexamineTabItem.addMouseListener(this);
        reexamineTabItem.setMaximumSize(new Dimension(390, 50));
        // Tạo các tab tùy theo quyền user
        tabItems.put(resultTabItem, resultFragment);
        tabItems.put(timeTableTabItem, timeTableFragment);
        tabItems.put(reexamineTabItem, reexamineFragment);
        if (UserService.getInstance().getLoggedUser().getPermission().equals(Permission.ADMIN)) {
            tabItems.put(courseTabItem, coursesFragment);
            tabItems.put(studentsTabItem, studentsFragment);
            tabItems.put(usersTabItem, userFragment);

        }
        tabItems.put(accountTabItem, accountFragment);

        for (JPanel tabItem : tabItems.keySet()) {
            Box tabItemBox = Box.createHorizontalBox();
            tabItemBox.add(tabItem);
            navigatePanel.add(tabItemBox);
        }

        pane.add(navigatePanel, BorderLayout.LINE_START);

        return pane;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedTabItem != null) {
            selectedTabItem.setBackground(selectedTabItem.getParent().getBackground());
            pane.remove(tabItems.get(selectedTabItem).getPanel());
        }

        selectedTabItem = (TabItem) e.getComponent();
        tabItems.get(selectedTabItem).build();
        pane.add(tabItems.get(selectedTabItem).getPanel(), BorderLayout.CENTER);
        pane.validate();
        pane.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}