package mystudy.Screen;

import java.awt.*;

import javax.swing.*;

import mystudy.Colors.Colors;
import mystudy.Components.RoundedButton;

public class DashboardScreen {

    public static JPanel build() {
        JPanel pane = new JPanel();
        pane.setBackground(Colors.getBackground());
        pane.setLayout(new BorderLayout());

        RoundedButton button = new RoundedButton("Click me", 30, 24);
        pane.add(button, BorderLayout.PAGE_START);

        return pane;
    }
}