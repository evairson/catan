package model;

import others.Constants;
import others.Music;
import view.ActionPlayerPanel;
import view.GamePanel;
import view.GameWindow;
import view.gamepanels.TradePanel;
import view.*;
import view.menu.MainMenu;

import java.awt.*;
import java.util.HashSet;

import network.NetworkObject;
import network.PlayerClient;
import network.NetworkObject.TypeObject;
import javax.swing.*;

public class App {
    private GamePanel gamePanel;
    private static ActionPlayerPanel actionPlayer;
    private EndPanel endPanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private GameBoard board;
    private Game game;
    private MainMenu mainMenu;
    private PlayerClient player;
    private boolean playing;
    private BackgroundPanel background;

    public GameBoard getBoard() {
        return board;
    }

    public static ActionPlayerPanel getActionPlayerPanel() {
        return actionPlayer;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
        board.setApp(this);
    }

    public App(PlayerClient playerClient) {
        player = playerClient;
        player.setApp(this);
        mainMenu = new MainMenu(this);
        this.gameWindow = new GameWindow(mainMenu);
        mainMenu.requestFocus();
    }

    public void createNewGame(Game game) {
        this.game = game;
        game.setPlayerClient(player);
        this.background = new BackgroundPanel();
        actionPlayer = new ActionPlayerPanel(this);
        this.gamePanel = new GamePanel(this);
        this.gameWindow.addPanels(actionPlayer, this.gamePanel, background);
        actionPlayer.update();
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
    public boolean isPlaying() {
        return playing;
    }
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void tryStartGame() {
        try {
            NetworkObject gameObject;
            gameObject = new NetworkObject(TypeObject.Message, "tryStartGame", player.getId(), null);
            player.getOut().writeUnshared(gameObject);
            player.getOut().flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void startGame(HashSet<Player> hashSet) {
        try {
            game = new Game(hashSet);
            System.out.println(game.getBoard() == null);
            NetworkObject gameObject = new NetworkObject(TypeObject.Game, "startGame", player.getId(), game);
            player.getOut().writeUnshared(gameObject);
            player.getOut().flush();
            System.out.println("ok");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void addMessage(String message) {
        ((ChatPanel) actionPlayer.getChat()).addMessage(message);
    }

    public void addPanels() {
        game.initialiseGameAfterTransfer(this);
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

    public PlayerClient getPlayer() {
        return player;
    }
}
