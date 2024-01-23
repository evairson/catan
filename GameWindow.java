
import javax.swing.*;

import java.awt.*;

public class GameWindow{
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        //get la taille de ton écran


        jframe = new JFrame("JSP ENCORE MDR");
        gd.setFullScreenWindow(jframe);

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
