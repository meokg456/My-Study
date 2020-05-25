package Application.Routes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

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

    public void route(JFrame window, String routeName) throws InterruptedException {
        Container pane = window.getContentPane();
        pane.removeAll();
        pane.setLayout(new BorderLayout());
        pane.add(routes.get(routeName), BorderLayout.CENTER);
        window.repaint();
        window.pack();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
