package mystudy;

import javax.swing.*;

import mystudy.Connector.DatabaseService;
import mystudy.Routes.Routes;
import mystudy.Screen.DashboardScreen;
import mystudy.Screen.LoginScreen;

public class Main {
    private static JFrame window = new JFrame();

    public static JFrame getWindow() {
        return window;
    }

    public static void main(String[] args) {
        DatabaseService.init();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("My Study");

        window.setVisible(true);
        window.pack();

        Routes.getInstance().getRoutes().put("Login", new LoginScreen());
        Routes.getInstance().getRoutes().put("Dashboard", new DashboardScreen());
        Routes.getInstance().route("Login");
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}