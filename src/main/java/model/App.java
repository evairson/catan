package model;

import others.Constants;
import others.Music;
import others.Tutorial;
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
    private OptionPanel optionPanel;
    private Tutorial tutorial;
    private static GameWindow gameWindow;
    private Thread gameThread;
    private GameBoard board;
    private static Game game;
    private static MainMenu mainMenu;
    private PlayerClient player;
    private static boolean playing;
    private boolean hasD20 = false;
    private static BackgroundPanel background;
    private static Boolean botSoloMode = false;

    public boolean isHasD20() {
        return hasD20;
    }

    public void checkD20() {
        hasD20 = optionPanel.d20isSelected();
    }

    public GameBoard getBoard() {
        return board;
    }

    public static ActionPlayerPanel getActionPlayerPanel() {
        return actionPlayer;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public OptionPanel getOptionPanel() {
        return optionPanel;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
        board.setApp(this);
    }

    public App(Player p) {
        mainMenu = new MainMenu(this, p);
        optionPanel = new OptionPanel();
        tutorial = new Tutorial();
        App.gameWindow = new GameWindow(mainMenu, optionPanel, tutorial);
        mainMenu.requestFocus();
    }

    public static boolean getBotSoloMode() {
        return botSoloMode;
    }

    public static void setBotSoloMode() {
        botSoloMode = true;
    }

    public App(PlayerClient playerClient) {
        player = playerClient;
        player.setApp(this);
        mainMenu = new MainMenu(this, null);
        optionPanel = new OptionPanel();
        tutorial = new Tutorial();
        if (playerClient != null) {
            ((PlayerClient) player).setApp(this);
            mainMenu = new MainMenu(this, null);
        } else {
            mainMenu = new MainMenu(this, player);
        }
        App.gameWindow = new GameWindow(mainMenu, optionPanel, tutorial);
        mainMenu.requestFocus();
    }

    public App() {
        mainMenu = new MainMenu(this, player);
        optionPanel = new OptionPanel();
        tutorial = new Tutorial();
        App.gameWindow = new GameWindow(mainMenu, optionPanel, tutorial);
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

        gameWindow.getContentPane().add(panelGame, "actionPlayerPanel");

    }

    public void update() {
        if (playing) {
            game.update();
            checkWin();
        }
        Music.update();
    }

    public static void checkWin() {
        if (game.getPlayerClient().hasWon(game)) {

            game.setGameHasEnded(true);
            endPanel = new EndPanel(true, game.getPlayerClient(), game);
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
        gamePanel.repaint();
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
