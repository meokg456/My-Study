package mystudy;

import javax.swing.*;

import mystudy.Routes.Routes;
import mystudy.Screen.LoginScreen;

public class Main {
    private static JFrame window = new JFrame();

    public static JFrame getWindow() {
        return window;
    }

    public static void main(String[] args) throws InterruptedException {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("My Study");

        window.setVisible(true);
        window.pack();

        Routes.getInstance().getRoutes().put("Login", LoginScreen.build());
        Routes.getInstance().route(window, "Login");

    }
}