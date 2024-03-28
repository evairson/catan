package view;

import others.Constants;
import view.gamepanels.TradePanel;
import view.menu.MainMenu;

import javax.swing.*;

import controler.MouseInputs;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;

    private ActionPlayerPanel actionPlayer;

    public ActionPlayerPanel getActionPlayer() {
        return actionPlayer;
    }

    private MainMenu mainMenu;
    private TradePanel tradePanel;
    private BackgroundPanel background;
    private CardLayout layout;

    public GameWindow(MainMenu mainMenu) {
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

        //Le layout de la JFrame est un CardLayout, qui permet de changer facilement de JPanel
        CardLayout layout = new CardLayout();
        this.layout = layout;
        this.getContentPane().setLayout(layout);

        this.getContentPane().add(mainMenu, "mainMenu");
        //add(actionPlayer, BorderLayout.CENTER);
        //GameBoard board = new GameBoard(null);
        //add(board);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.Game.WIDTH, Constants.Game.HEIGHT);
        setVisible(true);
        setResizable(false);
    }

    public void addPanels(ActionPlayerPanel actionPlayer, GamePanel gamePanel, BackgroundPanel background) {
        this.actionPlayer = actionPlayer;
        this.gamePanel = gamePanel;
        this.background = background;

    }
    public void close(int i) {
        System.exit(i);
    }

    @Override
    public CardLayout getLayout() {
        return layout;
    }
}
