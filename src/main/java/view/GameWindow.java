package view;

import others.Constants;

import javax.swing.*;

import java.awt.*;

public class GameWindow{
    public JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        //get la taille de ton écran

        jframe = new JFrame("JSP ENCORE MDR");
        gd.setFullScreenWindow(jframe);

        GraphicsDevice[] gds = ge.getScreenDevices();
        if(gds.length > 1) { // S'il y a plus d'un écran, ça lance sur le deuxième écran
            Rectangle bounds = gds[0].getDefaultConfiguration().getBounds(); // Récupère les limites du deuxième écran
            jframe.setLocation(bounds.x + (bounds.width - jframe.getWidth()) / 2, bounds.y + (bounds.height - jframe.getHeight()) / 2);
        } else {
            jframe.setLocationRelativeTo(null);
        } //full useless si un écran :)

        Constants.Game.WIDTH = jframe.getWidth();
        Constants.Game.HEIGHT = jframe.getHeight();


        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setVisible(true);

        System.out.println(Constants.Game.WIDTH + " " + Constants.Game.HEIGHT);
   }
    public void close(int i){
        System.exit(i);
    }
}
