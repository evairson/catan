package view;

import others.Constants;
import view.menu.MainMenu;

import javax.swing.*;

import java.awt.*;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;
    private MainMenu mainMenu;

    public GameWindow(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        GraphicsDevice[] gds = ge.getScreenDevices();
        if (gds.length > 1) { // S'il y a plus d'un écran, ça lance sur le deuxième écran
            // Récupère les limites du deuxième écran
            Rectangle bounds = gds[0].getDefaultConfiguration().getBounds();
            int borderX = bounds.x + (bounds.width - getWidth()) / 2;
            int borderY = bounds.y + (bounds.height - getHeight()) / 2;
            setLocation(borderX, borderY);

        } else {
            setLocationRelativeTo(null);
        } //full useless si un écran :)

        mainMenu = new MainMenu(this);
        setLayout(new BorderLayout());
        add(mainMenu, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.Game.WIDTH, Constants.Game.HEIGHT);
        setVisible(true);
        setResizable(false);

        System.out.println(Constants.Game.WIDTH + " " + Constants.Game.HEIGHT);
    }
    public void close(int i) {
        System.exit(i);
    }
}
