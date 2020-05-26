package mystudy;

import javax.swing.*;
import mystudy.Routes.Routes;
import mystudy.Screen.DashboardScreen;
import mystudy.Screen.LoginScreen;

public class Main {
    private static JFrame window = new JFrame();

    public static JFrame getWindow() {
        return window;
    }

    public static void main(String[] args) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("My Study");

        window.setVisible(true);
        window.pack();

        Routes.getInstance().getRoutes().put("Login", LoginScreen.build());
        Routes.getInstance().getRoutes().put("Dashboard", DashboardScreen.build());
        Routes.getInstance().route("Login");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}