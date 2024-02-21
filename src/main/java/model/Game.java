package model;

import others.Constants;
import others.ListPlayers;
import view.ActionPlayerPanel;
import view.GamePanel;
import view.GameState;
import view.GameWindow;
import view.menu.MainMenu;
import model.tiles.TileVertex;
import model.tiles.TileEdge;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;

public class Game implements Runnable {
    private GamePanel gamePanel;
    private ActionPlayerPanel actionPlayer;
    private GameWindow gameWindow;
    private Thread gameThread;
    private static GameBoard board;
    private Playing playing;
    private MainMenu mainMenu;

    private ListPlayers players; // ListPlayers extends ArrayList

    // players has a currentPlayer not necessary to have an attribut for this.
    public Player getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    public ListPlayers getPlayers() {
        return players;
    }

    public static GameBoard getBoard() {
        return board;
    }

    public static void setBoard(GameBoard board) {
        Game.board = board;
    }

    public Game() {
        Player player1 = new Player(Player.Color.RED, "Player1");
        Player player2 = new Player(Player.Color.YELLOW, "Player2");
        Player player3 = new Player(Player.Color.BLUE, "Player3");
        Player player4 = new Player(Player.Color.GREEN, "Player4");
        players = new ListPlayers(0, player1, player2, player3, player4);

        mainMenu = new MainMenu(this);
        gamePanel = new GamePanel(this);
        actionPlayer = new ActionPlayerPanel(this);
        gameWindow = new GameWindow(gamePanel, actionPlayer, mainMenu);

        playing = new Playing();
        board = playing.getBoard();
        playing.setGame(this);
        mainMenu.requestFocus();
        // add a mouse click listener to the gamePanel
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playing.mouseClicked(e);
            }
        });

        startGameLoop();
    }

    public void buildCityButtonAction() {
        if (Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) {
            if (getCurrentPlayer().hasColony()) {
                System.out.println("You can build a city");
                if (board.isLookingForVertex()) {
                    board.setLookingForVertex(!board.isLookingForVertex());
                    board.setPlacingCity(false);
                    board.setPlacingRoad(false);
                    board.setPlacingColony(false);
                } else {
                    board.setPlacingCity(true);
                    board.setPlacingRoad(false);
                    board.setPlacingColony(false);
                    board.setLookingForVertex(true);
                }
                if (board.isLookingForEdge()) {
                    board.setLookingForEdge(!board.isLookingForEdge());
                    board.setPlacingRoad(false);
                    board.setPlacingColony(false);
                }
            }
        }
    }

    public void buildColonyButtonAction() {
        if (Constants.BuildingCosts.canBuildColony(getCurrentPlayer().getResources())) {
            if (board.isLookingForVertex()) {
                board.setLookingForVertex(!board.isLookingForVertex());
                board.setPlacingCity(false);
                board.setPlacingRoad(false);
                board.setPlacingColony(false);
            } else {
                board.setPlacingCity(false);
                board.setPlacingRoad(false);
                board.setPlacingColony(true);
                board.setLookingForVertex(true);
            }
            if (board.isLookingForEdge()) {
                board.setLookingForEdge(!board.isLookingForEdge());
                board.setPlacingRoad(false);
                board.setPlacingCity(false);
            }
        }
    }

    public void buildRoadButtonAction() {
        if (Constants.BuildingCosts.canBuildRoad(getCurrentPlayer().getResources())) {
            if (board.isLookingForEdge()) {
                board.setLookingForEdge(!board.isLookingForEdge());
                board.setPlacingCity(false);
                board.setPlacingColony(false);
                board.setPlacingRoad(false);
            } else {
                board.setPlacingCity(false);
                board.setPlacingColony(false);
                board.setPlacingRoad(true);
                board.setLookingForEdge(true);
            }
            if (board.isLookingForVertex()) {
                board.setLookingForVertex(!board.isLookingForVertex());
                board.setPlacingCity(false);
                board.setPlacingColony(false);
            }
        }
    }

    public void buildColony() {
        if (board.isLookingForVertex()) {
            TileVertex cVertex = board.getClosestTileVertex();
            getCurrentPlayer().buildColony(cVertex);
        }
        // rajouter un if ça a marché (transformer Player.buildColony en boolean)
        board.setLookingForVertex(false);
        board.setPlacingColony(false);
    }

    public void buildRoad() {
        if (board.isLookingForEdge()) {
            TileEdge cEdge = board.getClosestTileEdge();
            getCurrentPlayer().buildRoad(cEdge);
        }
        // rajouter un if ça a marché (transformer Player.buildRoad en boolean)
        board.setLookingForEdge(false);
        board.setPlacingRoad(false);
    }

    public void buildCity() {
        if (board.isLookingForVertex()) {
            TileVertex cVertex = board.getClosestTileVertex();
            getCurrentPlayer().buildCity(cVertex);
        }
        // rajouter un if ça a marché (transformer Player.buildCity en boolean)
        board.setLookingForVertex(false);
        board.setPlacingCity(false);
    }

    public Playing getPlaying() {
        return playing;
    }

    public final GamePanel getGamePanel() {
        return gamePanel;
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void addPanels() {
        actionPlayer.add(gamePanel);
        gameWindow.add(actionPlayer);
    }

    public void update() {
    }

    public void render(Graphics g) {
        switch (GameState.getState()) {
            case Playing:
                playing.draw(g);
                break;
            case Menu:
                break; // à faire
            default:
        }
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

    // Player action : -----------------

    public void endTurn() {
        players.next();
    }

}
