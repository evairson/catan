package model;

import others.Constants;
import others.Music;
import view.ActionPlayerPanel;
import view.GamePanel;
import view.GameWindow;
import view.*;
import view.menu.MainMenu;

import java.awt.*;

public class App implements Runnable {
    private GamePanel gamePanel;
    private ActionPlayerPanel actionPlayer;
    private EndPanel endPanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private static GameBoard board;
    private Game game;
    private MainMenu mainMenu;
    private boolean playing;
    public static GameBoard getBoard() {
        return board;
    }
    public static void setBoard(GameBoard board) {
        App.board = board;
    }

    public App() {
        mainMenu = new MainMenu(this);
        this.gameWindow = new GameWindow(mainMenu);
        mainMenu.requestFocus();
    }
    public void createNewGame() {
        this.game = new Game(this);
        this.actionPlayer = new ActionPlayerPanel(this);
        this.gamePanel = new GamePanel(this);
        this.gameWindow.addPanels(this.actionPlayer, this.gamePanel);
        actionPlayer.update();
        startGameLoop();
    }

    public Game getGame() {
        return game;
    }

    public ActionPlayerPanel getActionPlayerPanel() {
        return actionPlayer;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
    public final GamePanel getGamePanel() {
        return gamePanel;
    }
    public boolean isPlaying() {
        return playing;
    }
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void startGameLoop() {
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
    }

    public void update() {
        if (playing) {
            game.update();
            checkWin();
            Music.update();
        }
    }
    public void checkWin() {
        if (game.getCurrentPlayer().hasWon()) {
            endPanel = new EndPanel(this, true, game.getCurrentPlayer());
            gameWindow.getContentPane().add(endPanel, "endPanel");
            stopGameLoop();
            endPanel.updatePanel();
            Container contentPane = getGameWindow().getContentPane();
            CardLayout layout = getGameWindow().getLayout();
            playing = false;
            layout.show(contentPane, "endPanel");
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
