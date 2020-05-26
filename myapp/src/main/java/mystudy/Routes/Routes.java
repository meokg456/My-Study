package mystudy.Routes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import mystudy.Main;

public class Routes {
    private static Routes instance;

    public static Routes getInstance() {
        if (instance == null) {
            instance = new Routes();
        }
        return instance;
    }

    private Map<String, JPanel> routes = new HashMap<>();

    public Map<String, JPanel> getRoutes() {
        return routes;
    }

    public void route(String routeName) {
        JFrame window = Main.getWindow();
        Container pane = window.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.removeAll();

        pane.add(routes.get(routeName), BorderLayout.CENTER);
        window.validate();
        window.repaint();
    }
}
