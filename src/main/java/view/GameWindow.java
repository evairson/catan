package view;

import others.Constants;

import javax.swing.*;

import java.awt.*;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(view.GamePanel gamePanel) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        //get la taille de ton écran

        jframe = new JFrame("JSP ENCORE MDR");
        gd.setFullScreenWindow(jframe);

        GraphicsDevice[] gds = ge.getScreenDevices();
        if (gds.length > 1) {
            // S'il y a plus d'un écran, ça lance sur le deuxième écran
            Rectangle bounds = gds[0].getDefaultConfiguration().getBounds();
            // Récupère les limites du deuxième écran
            int x = bounds.x + (bounds.width - jframe.getWidth()) / 2;
            int y = bounds.y + (bounds.height - jframe.getHeight()) / 2;
            jframe.setLocation(x, y);
        } else {
            jframe.setLocationRelativeTo(null);
        } //full useless si un écran :)

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setVisible(true);

        System.out.println(Constants.Game.WIDTH + " " + Constants.Game.HEIGHT);
    }
    public void close(int i) {
        System.exit(i);
    }
}
