package view;

import others.Constants;
import start.Main;
import view.menu.MainMenu;

import javax.swing.*;

import model.Game;
import model.GameBoard;

import java.awt.*;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;

    private ActionPlayerPanel actionPlayer;
    private MainMenu mainMenu;

    public GameWindow(GamePanel gamePanel, ActionPlayerPanel actionPlayer, MainMenu mainMenu) {
        this.actionPlayer = actionPlayer;
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

        this.mainMenu = mainMenu;
        setLayout(null);
        add(mainMenu);
        //add(actionPlayer, BorderLayout.CENTER);
        //GameBoard board = new GameBoard(null);
        //add(board);

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
