package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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

import java.io.Serializable;
import java.util.HashSet;

public class Game implements StateMethods, Serializable {
    private GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;
    private boolean resourcesGiven;
    private PlayerClient playerClient;

    Game(HashSet<Player> playersSet) {
        for (Player player : playersSet) {
            playersSet.add(player);
        }
        players = new ListPlayers(0, playersSet);
        Point point1 = new Point(400, 400);
        Point point2 = new Point(70, 70);
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        thief = new Thief();
        board = new GameBoard(layout, thief);

        stack = new CardStack();
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

    public void endTurn() {
        players.next();
        resourcesGiven = false;
        App.getActionPlayerPanel().update();
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
        board.setThiefMode(b);
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
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
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
            board.changeThief(e);
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

    public void networkBuildColony() {
        TileVertex cVertex = null;
        if (board.isLookingForVertex()) {
            cVertex = board.getClosestTileVertex();
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

    public void initialiseGameAfterTransfer() {
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
