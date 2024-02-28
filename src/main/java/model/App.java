package model;

import others.Constants;
import view.ActionPlayerPanel;
import view.GamePanel;
import view.GameWindow;
import view.menu.MainMenu;
import view.TradePanel;

import java.awt.*;
import java.util.HashSet;

import network.NetworkObject;
import network.PlayerClient;
import network.NetworkObject.TypeObject;

public class App implements Runnable {
    private GamePanel gamePanel;
    private ActionPlayerPanel actionPlayer;
    private GameWindow gameWindow;
    private Thread gameThread;
    private static GameBoard board;
    private Game game;
    private MainMenu mainMenu;
    private PlayerClient player;

    public static GameBoard getBoard() {
        return board;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public static void setBoard(GameBoard board) {
        App.board = board;
    }

    public App(PlayerClient playerClient) {
        player = playerClient;
        player.setApp(this);
        mainMenu = new MainMenu(this);
        gameWindow = new GameWindow(null, null, mainMenu);

        mainMenu.requestFocus();

        actionPlayer.update();

        startGameLoop();
    }

    public Game getGame() {
        return game;
    }
    public GameWindow getGameWindow() {
        return gameWindow;
    }
    public final GamePanel getGamePanel() {
        return gamePanel;
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void tryStartGame() {
        try {
            NetworkObject gameObject;
            gameObject = new NetworkObject(TypeObject.Message, "tryStartGame", player.getId(), null);
            player.getOut().writeUnshared(gameObject);
            player.getOut().flush();
            System.out.println("ca c'est bon");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void startGame(HashSet<String> hashSet) {
        try {
            game = new Game(hashSet);
            System.out.println(game.getBoard() == null);
            NetworkObject gameObject = new NetworkObject(TypeObject.Game, "startGame", player.getId(), game);
            player.getOut().writeUnshared(gameObject);
            player.getOut().flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void addPanels(Game game) {
        this.game = game;
        gamePanel = new GamePanel(this);
        actionPlayer = new ActionPlayerPanel(this);
        actionPlayer.add(gamePanel);
        gameWindow.getContentPane().add(actionPlayer, "actionPlayerPanel");
    }
    public void createTradePanel() {
        TradePanel tradePanel = new TradePanel(this.game, this.gameWindow);

        //Ajoute la fenetre de trade
        gameWindow.getContentPane().add(tradePanel, "tradePanel");
    }

    public void update() {
        game.update();
    }

    public void render(Graphics g) {
        game.draw(g);
    }

    @Override
    public final void run() {

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            double timePerFrame = Constants.Number.DOUBLE_BILLION / Constants.Game.FPS_SET;
            double timePerUpdate = Constants.Number.DOUBLE_BILLION / Constants.Game.UPS_SET;
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                mainMenu.repaint();
                if (actionPlayer != null) {
                    actionPlayer.repaint();
                    gamePanel.repaint();
                }
                gameWindow.repaint();
                gameWindow.revalidate();
                deltaF--;
                frames++;
            }

            if (System.currentTimeMillis() - lastCheck >= Constants.Number.SECOND) {
                lastCheck = System.currentTimeMillis();
                // System.out.println("FPS :" + frames + " | Ups " + updates);
                updates = 0;
                frames = 0;
            }

        }
    }
}
