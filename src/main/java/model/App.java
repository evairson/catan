package model;

import others.Constants;
import others.Music;
import view.*;
import view.menu.MainMenu;

import java.awt.*;
import java.util.HashSet;

import network.NetworkObject;
import network.PlayerClient;
import network.NetworkObject.TypeObject;
import javax.swing.*;

public class App {
    private static GamePanel gamePanel;
    private static ActionPlayerPanel actionPlayer;
    private static EndPanel endPanel;
    private static GameWindow gameWindow;
    private Thread gameThread;
    private GameBoard board;
    private static Game game;
    private static MainMenu mainMenu;
    private Player player;
    private static boolean playing;
    private boolean hasD20 = true;
    private static BackgroundPanel background;

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

    public boolean isHasD20() {
        return hasD20;
    }

    public App(Player playerClient) {
        player = playerClient;
        if (playerClient instanceof PlayerClient) {
            ((PlayerClient) player).setApp(this);
            mainMenu = new MainMenu(this, null);
        } else {
            mainMenu = new MainMenu(this, player);
        }
        App.gameWindow = new GameWindow(mainMenu);
        mainMenu.requestFocus();
    }

    public void createNewGame(Game game) {
        App.game = game;
        game.setPlayerClient(player);
        App.background = new BackgroundPanel();
        actionPlayer = new ActionPlayerPanel(this);
        gamePanel = new GamePanel(this);
        App.gameWindow.addPanels(actionPlayer, gamePanel, background);
    }

    public Game getGame() {
        return game;
    }

    public static GameWindow getGameWindow() {
        return gameWindow;
    }
    public static GamePanel getGamePanel() {
        return gamePanel;
    }
    public boolean isPlaying() {
        return playing;
    }
    public void setPlaying(boolean playing) {
        App.playing = playing;
    }

    public void tryStartGame() {
        try {
            NetworkObject gameObject;
            gameObject = new NetworkObject(TypeObject.Message, "tryStartGame", player.getId(), null);
            ((PlayerClient) player).getOut().writeUnshared(gameObject);
            ((PlayerClient) player).getOut().flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void startGame(HashSet<Player> hashSet) {
        try {
            game = new Game(hashSet);
            NetworkObject gameObject = new NetworkObject(TypeObject.Game, "startGame", player.getId(), game);
            ((PlayerClient) player).getOut().writeUnshared(gameObject);
            ((PlayerClient) player).getOut().flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public static void addMessage(String message) {
        ((ChatPanel) actionPlayer.getChat()).addMessage(message);
    }

    public void addMessageColor(String message, Color color) {
        (actionPlayer.getLogChat()).addMessageColor(message, color);
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
    public static void checkWin() {
        if (game.getCurrentPlayer().hasWon()) {
            endPanel = new EndPanel(true, game.getCurrentPlayer());
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

    public Player getPlayer() {
        return player;
    }

    public boolean hasD20() {
        return hasD20;
    }

    public static ActionPlayerPanel getActionPlayer() {
        return actionPlayer;
    }
}
