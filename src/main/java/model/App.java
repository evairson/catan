package model;

import others.Constants;
import others.Music;
import view.ActionPlayerPanel;
import view.GamePanel;
import view.GameWindow;
import view.*;
import view.menu.MainMenu;

import java.awt.*;
import javax.swing.*;

public class App {
    private GamePanel gamePanel;
    private ActionPlayerPanel actionPlayer;
    private EndPanel endPanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private GameBoard board;
    private Game game;
    private MainMenu mainMenu;
    private boolean playing;
    private BackgroundPanel background;

    public GameBoard getBoard() {
        return board;
    }
    public void setBoard(GameBoard board) {
        this.board = board;
        board.setApp(this);
    }

    public App() {
        mainMenu = new MainMenu(this);
        this.gameWindow = new GameWindow(mainMenu);
        mainMenu.requestFocus();
    }
    public void createNewGame() {
        this.game = new Game(this);
        this.background = new BackgroundPanel();
        this.actionPlayer = new ActionPlayerPanel(this);
        this.gamePanel = new GamePanel(this);
        this.gameWindow.addPanels(this.actionPlayer, this.gamePanel, background);
        actionPlayer.update();
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

    public void addPanels() {
        JPanel panelGame = new JPanel();
        panelGame.setLayout(null);
        panelGame.setBounds(0, 0, Constants.Game.BASE_WIDTH, Constants.Game.BASE_HEIGHT);
        panelGame.add(actionPlayer, 0);
        panelGame.add(gamePanel, 1);
        panelGame.add(background, 2);
        gameWindow.getContentPane().add(mainMenu, "mainMenu");
        gameWindow.getContentPane().add(panelGame, "actionPlayerPanel");
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

}
