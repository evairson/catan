package model;

import others.Constants;
import view.*;
import view.menu.MainMenu;

import java.awt.*;

public class App implements Runnable {
    private GamePanel gamePanel;
    private ActionPlayerPanel actionPlayer;
    private WinPanel winPanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private static GameBoard board;
    private Game game;
    private MainMenu mainMenu;
    public static GameBoard getBoard() {
        return board;
    }


    public static void setBoard(GameBoard board) {
        App.board = board;
    }

    public App() {
        mainMenu = new MainMenu(this);
        gamePanel = new GamePanel(this);
        createNewGame();
        actionPlayer = new ActionPlayerPanel(this);
        winPanel = new WinPanel(this);
        gameWindow = new GameWindow(gamePanel, actionPlayer, mainMenu);

        mainMenu.requestFocus();

        actionPlayer.update();

        startGameLoop();
    }
    public void createNewGame() {
        this.game = new Game();
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
    private void stopGameLoop() {
        gameThread.interrupt();
    }

    public void addPanels() {
        actionPlayer.add(gamePanel);
        gameWindow.getContentPane().add(mainMenu, "mainMenu");
        gameWindow.getContentPane().add(actionPlayer, "actionPlayerPanel");
        gameWindow.getContentPane().add(winPanel, "winPanel");
    }
    public void createTradePanel() {
//        TradePanel tradePanel = new TradePanel(this.game, this.gameWindow);

        //Ajoute la fenetre de trade
//        gameWindow.getContentPane().add(tradePanel, "tradePanel");
    }

    public void update() {
        game.update();
        checkWin();
    }
    public void checkWin() {
        if (game.getCurrentPlayer().hasWon()) {
            stopGameLoop();
            Container contentPane = getGameWindow().getContentPane();
            CardLayout layout = getGameWindow().getLayout();
            layout.show(contentPane, "winPanel");
        }
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
                actionPlayer.repaint();
                gamePanel.repaint();
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
