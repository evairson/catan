package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import model.buildings.Building;
import model.buildings.Colony;
import model.cards.CardStack;
import model.geometry.Layout;
import model.geometry.Point;
import model.tiles.Tile;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import network.NetworkObject;
import network.PlayerClient;
import network.NetworkObject.TypeObject;
import others.Constants;
import others.ListPlayers;
import start.Main;
//import view.utilities.Resolution;
import view.TileType;

import java.io.Serializable;
import java.util.HashSet;

public class Game implements StateMethods, Serializable {
    private GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;
    private boolean resourcesGiven;
    private PlayerClient playerClient;
    private boolean start = true;
    private boolean backwards = false;
    private boolean playingVoleur = false;
    private App app;
    private boolean blankTurn = false;
    private boolean monoWaiting = false;
    private int yearOfPlentyWaiting = 0;

    Game(HashSet<Player> playersSet) {
        for (Player player : playersSet) {
            playersSet.add(player);
        }
        players = new ListPlayers(0, playersSet);
        players.getCurrentPlayer().setFreeColony(true);
        double scaleFactorX = (double) Constants.Game.WIDTH / Constants.Game.BASE_WIDTH;
        double scaleFactorY = (double) Constants.Game.HEIGHT / Constants.Game.BASE_HEIGHT;
        System.out.println(scaleFactorX + " et " + scaleFactorY);
        Point point1 = new Point(
                (int) (267 * scaleFactorX),
                (int) (267 * scaleFactorY)
        );
        System.out.println((int) (267 * scaleFactorX) + " et  ; " + (int) (47 * scaleFactorX));
        Point point2 = new Point(
                (int) (47 * scaleFactorX),
                (int) (47 * scaleFactorY)
        );
//        Point point2 = new Point((int) (93 / Resolution.divider()), (int) (93 / Resolution.divider()));
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        thief = new Thief();
        board = new GameBoard(layout, thief, this);
        stack = new CardStack();
    }

    public App getApp() {
        return app;
    }

    public CardStack getStack() {
        return stack;
    }

    public Thief getThief() {
        return thief;
    }

    public void serverEndTurn() {
        if (Main.hasServer()) {
            try {
                int id = playerClient.getId();
                NetworkObject object = new NetworkObject(TypeObject.Message, "changeTurn", id, null);
                playerClient.getOut().writeUnshared(object);
                playerClient.getOut().flush();
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            endTurn();
        }
    }

    public boolean getBlankTurn() {
        return blankTurn;
    }

    public void setMonoWaiting(boolean b) {
        monoWaiting = b;
    }

    public void resourceClicked(TileType t) {
        if (monoWaiting) {
            monopolyPlay(t);
            return;
        }
        if (yearOfPlentyWaiting > 0) {
            getCurrentPlayer().addResource(t, 1);
            yearOfPlentyWaiting--;
        }
    }

    public void monopolyPlay(TileType t) {
        monoWaiting = false;
        int amount = 0;
        for (Player p: players) {
            if (p != getCurrentPlayer()) {
                amount += p.getResource(t);
                p.addResource(t, -p.getResource(t));
            }
        }
        getCurrentPlayer().addResource(t, amount);
        System.out.println("monopoly de " + amount + " " + t);
    }

    public boolean canPass() {
        if (!getCurrentPlayer().hasThrowDices() && !start && !backwards) {
            return false;
        }
        if ((start || backwards)
            && (getCurrentPlayer().getFreeRoad() || getCurrentPlayer().getFreeColony())) {
            return false;
        }
        if (playingVoleur) {
            return false;
        }
        if (App.getActionPlayerPanel().getCardPlayed()) {
            return false;
        }
        return true;
    }

    public void endTurn() {
        if (!canPass()) {
            return;
        }

        if (start || backwards) {
            ArrayList<Colony> colony = getCurrentPlayer().getColony();
            if (colony.size() >= 2) {
                for (Colony c: colony) {
                    for (Tile t : c.getVertex().getTiles()) {
                        getCurrentPlayer().addResource(t.getResourceType(), 1);
                    }
                }
            }
        }

        if (start && getCurrentPlayer().getName().equals("Player4")) {
            start = false;
            backwards = true;
        } else if (backwards && getCurrentPlayer().getName().equals("Player1")) {
            backwards = false;
        } else if (backwards) {
            players.prev();
        } else {
            players.next();
        }

        if (!start && !backwards && !blankTurn) {
            App.getActionPlayerPanel().getRollingDice().setButtonIsOn(true);
        }
        System.out.println("It's " + getCurrentPlayer() .getName() + "'s turn");
        resourcesGiven = false;
        App.getActionPlayerPanel().getRollingDice().newPlayer(getCurrentPlayer());

        if (start || backwards) {
            getCurrentPlayer().setFreeColony(true);
        }
    }

    public boolean canDraw() {
        if (blankTurn || !resourcesGiven) {
            return false;
        }
        int[] t = {0, 1, 1, 0, 1};
        return getCurrentPlayer().hasEnough(t) && !start && !backwards;
    }

    public ListPlayers getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setThiefMode(boolean b) {
        playingVoleur = b;
        board.setThiefMode(b);
        divideRessourcesByTwo();
    }

    public void draw(Graphics g) {
        board.draw(g);
    }

    public PlayerClient getPlayerClient() {
        return playerClient;
    }

    // Player action : -----------------

    public void mouseMoved(MouseEvent e) {
        board.mouseMoved(e);
    }

    @Override
    public void update() {
        lootResources();
    }

    public void divideRessourcesByTwo() {
        ListPlayers pChecks = (ListPlayers) players.clone();
        pChecks.remove(getCurrentPlayer());
        for (Player p : pChecks) {
            if (p.getResourcesSum() > p.getResourceCap()) {
                for (int i = 0; i < p.getResourcesSum() / 2; i++) {
                    p.removeOneRandom();
                }
            }
        }
    }

    public void lootResources() {
        if (getCurrentPlayer().hasThrowDices() && !resourcesGiven) {
            for (Player player : players) {
                for (Building b : player.getBuildings()) {
                    if (b instanceof Colony) {
                        Colony colony = (Colony) b;
                        for (Tile tile : colony.getVertex().getTiles()) {
                            if (tile.getDiceValue() == getCurrentPlayer().getDice()) {
                                if (colony.getIsCity()) {
                                    Integer number = player.getResources().get(tile.getResourceType());
                                    player.getResources().replace(tile.getResourceType(), number + 2);
                                    System.out.println("2 " + tile.getResourceType() + player.getName());
                                } else {
                                    Integer number = player.getResources().get(tile.getResourceType());
                                    player.getResources().replace(tile.getResourceType(), number + 1);
                                    System.out.println("1 " + tile.getResourceType() + player.getName());
                                }
                            }
                        }
                    }
                }
            }
            resourcesGiven = true;
        }
        App.getActionPlayerPanel().update();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (board.isPlacingCity()) {
            networkBuildCity();
            System.out.println("Building city");
        } else if (board.isPlacingColony()) {
            networkBuildColony();
            System.out.println("Building colony");
        } else if (board.isPlacingRoad()) {
            networkBuildRoad();
            System.out.println("Building road");
        } else if (board.getThiefMode()) {
            board.changeThief();
            board.setThiefModeEnd(true);
        }

        getCurrentPlayer().printResources();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Build methods ---------------
    public void buildCityButtonAction() {
        if (blankTurn) {
            return;
        }
        if ((Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) && resourcesGiven) {
            if (getCurrentPlayer().hasColony()) {
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
        if (blankTurn) {
            return;
        }
        if (((Constants.BuildingCosts.canBuildColony(getCurrentPlayer().getResources())) && resourcesGiven)
            || getCurrentPlayer().getFreeColony()) {
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
        if (blankTurn) {
            return;
        }
        if (((!Constants.BuildingCosts.canBuildRoad(getCurrentPlayer().getResources())) && resourcesGiven)
            || getCurrentPlayer().getFreeRoad()) {
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

    public void networkBuildColony() {
        TileVertex cVertex = null;
        if (board.isLookingForVertex()) {
            cVertex = board.getClosestTileVertex();
            if (board.isVertexTwoRoadsAwayFromCities(cVertex)) {
                getCurrentPlayer().buildColony(cVertex);
            }
        }
        if (Main.hasServer()) {
            if (cVertex != null) {
                try {
                    int id = playerClient.getId();
                    NetworkObject object = new NetworkObject(TypeObject.Board, "buildColony", id, cVertex);
                    playerClient.getOut().writeUnshared(object);
                    playerClient.getOut().flush();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        } else {
            if (cVertex != null) {
                buildColony(cVertex);
            }
        }
        // rajouter un if ça a marché (transformer Player.buildCity en boolean)
        board.setLookingForVertex(false);
        board.setPlacingCity(false);

    }

    public void buildColony(TileVertex cVertex) {
        TileVertex currentVertex = cVertex;
        for (TileVertex vertex : board.getVerticesMap().values()) {
            if (vertex.getId() == cVertex.getId()) {
                System.out.println("yeah !");
                currentVertex = vertex;
            }
        }
        getCurrentPlayer().buildColony(currentVertex);
    }

    public void networkBuildRoad() {
        TileEdge cEdge = null;
        if (board.isLookingForEdge()) {
            cEdge = board.getClosestTileEdge();
        }
        if (Main.hasServer()) {
            if (cEdge != null) {
                try {
                    int id = playerClient.getId();
                    NetworkObject object = new NetworkObject(TypeObject.Board, "buildRoad", id, cEdge);
                    playerClient.getOut().writeUnshared(object);
                    playerClient.getOut().flush();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        } else {
            if (cEdge != null) {
                buildRoad(cEdge);
            }
        }
        // rajouter un if ça a marché (transformer Player.buildRoad en boolean)
        board.setLookingForEdge(false);
        board.setPlacingRoad(false);
    }

    public void buildRoad(TileEdge cEdge) {
        TileEdge currentEdge = cEdge;
        for (TileEdge edge : board.getEdgeMap().values()) {
            if (edge.getId() == cEdge.getId()) {
                System.out.println("yeah !");
                currentEdge = edge;
            }
        }
        getCurrentPlayer().buildRoad(currentEdge);
    }

    public void networkBuildCity() {
        TileVertex cVertex = null;
        if (board.isLookingForVertex()) {
            cVertex = board.getClosestTileVertex();
            if (board.isVertexTwoRoadsAwayFromCities(cVertex)) {
                getCurrentPlayer().buildCity(cVertex);
            }
        }
        if (Main.hasServer()) {
            if (cVertex != null) {
                try {
                    int id = playerClient.getId();
                    NetworkObject object = new NetworkObject(TypeObject.Board, "buildCity", id, cVertex);
                    playerClient.getOut().writeUnshared(object);
                    playerClient.getOut().flush();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        } else {
            if (cVertex != null) {
                buildCity(cVertex);
            }
        }
        // rajouter un if ça a marché (transformer Player.buildCity en boolean)
        board.setLookingForVertex(false);
        board.setPlacingCity(false);

    }

    public void buildCity(TileVertex cVertex) {
        TileVertex currentVertex = cVertex;
        for (TileVertex vertex : board.getVerticesMap().values()) {
            if (vertex.getId() == cVertex.getId()) {
                System.out.println("yeah !");
                currentVertex = cVertex;
            }
        }
        getCurrentPlayer().buildCity(cVertex);
    }

    public void initialiseGameAfterTransfer(App app) {
        this.app = app;
        app.setBoard(board);
        board.initialiseBoardAfterTransfer();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == playerClient.getId()) {
                Boolean isCurrentPlayer = false;
                if (getCurrentPlayer() == players.get(i)) {
                    isCurrentPlayer = true;
                }
                players.set(i, playerClient);
                if (isCurrentPlayer) {
                    players.setCurrentPlayer(playerClient);
                }
            }
        }
    }

    public void setPlayerClient(PlayerClient player) {
        playerClient = player;
    }

    public boolean isMyTurn() {
        return playerClient.isMyTurn(this);
    }
}
