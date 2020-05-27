package mystudy.Routes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import mystudy.Main;
import mystudy.Screen.Screen;

public class Routes {
    private static Routes instance;

    public static Routes getInstance() {
        if (instance == null) {
            instance = new Routes();
        }
        return instance;
    }

    private Map<String, Screen> routes = new HashMap<>();

    public Map<String, Screen> getRoutes() {
        return routes;
    }

    public void route(String routeName) {
        JFrame window = Main.getWindow();
        Container pane = window.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.removeAll();

        pane.add(routes.get(routeName).build(), BorderLayout.CENTER);
        window.validate();
        window.repaint();
    }
}
