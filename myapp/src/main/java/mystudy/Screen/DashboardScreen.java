package mystudy.Screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;

import mystudy.Colors.Colors;
import mystudy.Components.IconButton;
import mystudy.Fonts.Fonts;
import mystudy.Fragment.AccountFragment;

public class DashboardScreen implements Screen {

    public JPanel build() {
        JPanel pane = new JPanel();
        pane.setBackground(Colors.getBackground());
        pane.setLayout(new BorderLayout());

        JPanel navigatePanel = new JPanel();
        navigatePanel.setLayout(new BoxLayout(navigatePanel, BoxLayout.Y_AXIS));
        navigatePanel.setBackground(Colors.getPrimary());
        navigatePanel.setPreferredSize(new Dimension(400, 0));
        navigatePanel.setBorder(new EmptyBorder(100, 0, 0, 0));

        JLabel dashboardLabel = new JLabel("Dashboard");
        dashboardLabel.setFont(new Font(Fonts.getFont().getName(), Font.PLAIN, 48));
        dashboardLabel.setForeground(Colors.getTextColor());
        dashboardLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        navigatePanel.add(dashboardLabel);
        navigatePanel.add(Box.createVerticalStrut(50));

        Map<JPanel, JPanel> tabItems = new LinkedHashMap<>();
        AccountFragment accountFragment = new AccountFragment();
        IconButton accountTabItem = new IconButton(new ImageIcon("myapp/src/main/java/mystudy/Icons/man.png"),
                "Account") {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setBackground(Colors.getSecondary());
                pane.add(accountFragment, BorderLayout.CENTER);
                pane.validate();
                pane.repaint();
            }
        };

        accountTabItem.setMaximumSize(new Dimension(600, 50));
        tabItems.put(accountTabItem, accountFragment);

        for (JPanel tabItem : tabItems.keySet()) {
            navigatePanel.add(tabItem);
        }

        pane.add(navigatePanel, BorderLayout.LINE_START);

        return pane;
    }
}