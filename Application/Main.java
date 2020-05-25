package Application;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import Application.Routes.Routes;
import Application.Screen.LoginScreen;

public class Main {
    private static JFrame window = new JFrame();

    public static void main(String[] args) throws InterruptedException, FontFormatException, IOException {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("My Study");

        window.setVisible(true);
        window.pack();

        Routes.getInstance().getRoutes().put("Login", LoginScreen.build());
        Routes.getInstance().route(window, "Login");

    }
}