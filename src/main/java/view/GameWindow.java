package view;

import others.Constants;
import others.Tutorial;
import view.gamepanels.TradePanel;
import view.menu.MainMenu;
import javax.swing.*;
import java.awt.*;

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

    public GameWindow(MainMenu mainMenu, OptionPanel optionPanel, Tutorial tutorial) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        GraphicsDevice[] gds = ge.getScreenDevices();
        this.mainMenu = mainMenu;

        //Le layout de la JFrame est un CardLayout, qui permet de changer facilement de JPanel
        CardLayout layout = new CardLayout();
        this.layout = layout;
        this.getContentPane().setLayout(layout);

        this.getContentPane().add(mainMenu, "mainMenu");
        this.getContentPane().add(optionPanel, "optionPanel");
        this.getContentPane().add(tutorial, "tutorial");
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
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
            new EventDispatcher(actionPlayer));

    }
    public void close(int i) {
        System.exit(i);
    }

    @Override
    public CardLayout getLayout() {
        return layout;
    }
}
