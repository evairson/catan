package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import model.buildings.Building;
import model.buildings.Colony;
import model.cards.CardStack;
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

import exceptionclass.ConstructBuildingException;

public class Game implements StateMethods, Serializable {
    private GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;
    private boolean resourcesGiven;
    private PlayerClient playerClient;
    private boolean start = true;
    private boolean backwards = false;
    private App app;
    private boolean blankTurn = false;
    private boolean monoWaiting = false;
    private int yearOfPlentyWaiting = 0;
    private Player first;

    public Game(HashSet<Player> playersSet) {
        for (Player player : playersSet) {
            playersSet.add(player);
        }
        players = new ListPlayers(0, playersSet);
        players.getCurrentPlayer().setFreeColony(true);

        thief = new Thief();
        board = new GameBoard(thief, this);
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

    public void setYearOfPlenty(int i) {
        yearOfPlentyWaiting = i;
    }

    public void resourceClicked(TileType t) {
        if (monoWaiting) {
            monopolyPlay(t);
            return;
        }
        if (yearOfPlentyWaiting > 0) {
            getCurrentPlayer().addResource(t, 1);
            System.out.println("gave " + t);
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
        app.getActionPlayerPanel().getResourcesPanel().updateResourceLabels(getCurrentPlayer());
        System.out.println("monopoly de " + amount + " " + t);
    }

    public boolean canPass() {
        Player p = getCurrentPlayer();
        if (p.getFreeRoad() > 0) {
            return false;
        }
        if (monoWaiting || yearOfPlentyWaiting > 0) {
            return false;
        }
        if (p.getFreeColony()) {
            return false;
        }
        if (!p.hasThrowDices() && !start && !backwards) {
            return false;
        }
        if (board.getThiefMode()) {
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

        if (start && getCurrentPlayer().last(this)) {
            start = false;
            backwards = true;
        } else if (backwards && getCurrentPlayer().first(this)) {
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
        App.getActionPlayerPanel().update();
        App.getGamePanel().repaint();
    }

    public boolean canDraw() {
        if (blankTurn || !resourcesGiven) {
            return false;
        }
        return getCurrentPlayer().hasEnough(Constants.BuildingCosts.CARD) && !start && !backwards;
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
                                } else {
                                    Integer number = player.getResources().get(tile.getResourceType());
                                    player.getResources().replace(tile.getResourceType(), number + 1);
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
        } else if (board.isPlacingColony()) {
            networkBuildColony();
        } else if (board.isPlacingRoad()) {
            networkBuildRoad();
        } else if (board.getThiefMode()) {
            board.changeThiefNetwork();
            board.setThiefModeEnd(true);
        }
        App.getGamePanel().repaint();
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

    /**
     * canBuildCity
     * Check if the player can build a city
     * it checks if the player has enough resources and if the resources have been given
     * @return true if the player can build a city, false otherwise
     * @see Constants.BuildingCosts.canBuildCity to see how the cost is calculated
     * 
     */
    public boolean canBuildCity() {
        if (blankTurn) {
            return false;
        }
        if (((Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) && resourcesGiven)) {
            return true;
        }
        return false;
    }

    /**
     * buildCityButtonAction
     * This method is called when the player clicks on the "build city" button
     * It checks if the player can build a city and if the player has a colony
     * If the player can build a city and has a colony, it sets the board to look for a vertex
     * If the board is already looking for a vertex, it sets the board to not look for a vertex
     * It also sets the board to not place a city, a road or a colony
     * If the board is looking for an edge, it sets the board to not look for an edge
     */
    public void buildCityButtonAction() {
        if (blankTurn) {
            return;
        }
        if (((Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) && resourcesGiven)) {
            System.out.println("First if");
            if (getCurrentPlayer().hasColony()) {
                System.out.println("Second if");
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

    /**
     * canBuildColony
     * Check if the player can build a colony
     * it checks if the player has enough resources and if the resources have been given
     * @return true if the player can build a colony, false otherwise
     * @see Constants.BuildingCosts.canBuildColony to see how the cost is calculated
     */
    public boolean canBuildColony() {
        if (blankTurn) {
            return false;
        }
        if (((Constants.BuildingCosts.canBuildColony(getCurrentPlayer().getResources())) && resourcesGiven)
            || getCurrentPlayer().getFreeColony()) {
            return true;
        }
        return false;
    }

    /**
     * buildColonyButtonAction
     * This method is called when the player clicks on the "build colony" button
     * It checks if the player can build a colony and if the resources have been given
     * If the player can build a colony and the resources have been given, it sets the board to look for a vertex
     * If the board is already looking for a vertex, it sets the board to not look for a vertex
     * It also sets the board to not place a city, a road or a colony
     * If the board is looking for an edge, it sets the board to not look for an edge
     */
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

    /**
     * canBuildRoad
     * Check if the player can build a road
     * it checks if the player has enough resources and if the resources have been given
     * @return true if the player can build a road, false otherwise
     * @see Constants.BuildingCosts.canBuildRoad to see how the cost is calculated
     */
    public boolean canBuildRoad() {
        if (blankTurn) {
            return false;
        }
        if (((Constants.BuildingCosts.canBuildRoad(getCurrentPlayer().getResources())) && resourcesGiven)
            || getCurrentPlayer().getFreeRoad() > 0) {
            return true;
        }
        return false;
    }

    /**
     * buildRoadButtonAction
     * This method is called when the player clicks on the "build road" button
     * It checks if the player can build a road and if the resources have been given
     * If the player can build a road and the resources have been given, it sets the board to look for an edge
     * If the board is already looking for an edge, it sets the board to not look for an edge
     * It also sets the board to not place a city, a road or a colony
     * If the board is looking for a vertex, it sets the board to not look for a vertex
     */
    public void buildRoadButtonAction() {
        if (blankTurn) {
            return;
        }
        if (((Constants.BuildingCosts.canBuildRoad(getCurrentPlayer().getResources())) && resourcesGiven)
            || getCurrentPlayer().getFreeRoad() > 0) {
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
            if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
                getCurrentPlayer().buildColony(cVertex);
            }
        }
        if (Main.hasServer()) {
            if (cVertex != null) {
                try {
                    int id = playerClient.getId();
                    NetworkObject object = new NetworkObject(TypeObject.Board, "buildColony",
                            id, cVertex.getId());
                    playerClient.getOut().writeUnshared(object);
                    playerClient.getOut().flush();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        } else {
            if (cVertex != null) {
                try {
                    buildColony(cVertex.getId());
                    App.getActionPlayerPanel().update();
                    App.getGamePanel().repaint();
                } catch (ConstructBuildingException e) {
                    ConstructBuildingException.messageError();
                }
            }
        }


    }

    /**
     * buildColony
     * This method is called when the player clicks on a vertex to build a colony
     * It checks if the player can build a colony and if the resources have been given
     * If the player can build a colony and the resources have been given, it builds a colony on the vertex
     * @param idVertex the id of the vertex where the player wants to build a colony
     * @throws ConstructBuildingException if the player can't build a colony on the vertex
     */
    public void buildColony(int idVertex) throws ConstructBuildingException {
        for (TileVertex vertex : board.getVertices()) {
            if (vertex.getId() == idVertex) {
                if (board.canPlaceColony(vertex, getCurrentPlayer())) {
                    board.setLookingForVertex(false);
                    board.setPlacingCity(false);
                    getCurrentPlayer().buildColony(vertex);
                    App.getGamePanel().repaint();
                }
                return;
            }
        }
        throw new ConstructBuildingException();
    }

    public void networkBuildRoad() {
        TileEdge cEdge = null;
        if (board.isLookingForEdge()) {
            cEdge = board.getClosestTileEdge();
        }
        if (Main.hasServer()) {
            if (cEdge != null) {
                if (board.canPlaceRoad(cEdge, getCurrentPlayer())) {
                    try {
                        int id = playerClient.getId();
                        NetworkObject object = new NetworkObject(TypeObject.Board, "buildRoad",
                                id, cEdge.getId());
                        playerClient.getOut().writeUnshared(object);
                        playerClient.getOut().flush();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        } else {
            if (cEdge != null) {
                //try {
                if (board.canPlaceRoad(cEdge, getCurrentPlayer())) {
                    getCurrentPlayer().buildRoad(cEdge);
                }
                /* } catch (ConstructBuildingException e) {
                    ConstructBuildingException.messageError();
                }*/
            }
        }
        // rajouter un if ça a marché (transformer Player.buildRoad en boolean)
        board.setLookingForEdge(false);
        board.setPlacingRoad(false);
    }

    /**
     * buildRoad
     * @param idEdge the id of the edge where the player wants to build a road
     * @throws ConstructBuildingException if the player can't build a road on the edge
     * @see ConstructBuildingException
     * @see Player.buildRoad
     * @see TileEdge
     */
    public void buildRoad(int idEdge) throws ConstructBuildingException {
        for (TileEdge edge : board.getEdgeMap().values()) {
            if (edge.getId() == idEdge) {
                System.out.println("yeah !");
                board.setLookingForVertex(false);
                board.setPlacingCity(false);
                getCurrentPlayer().buildRoad(edge);
                App.getActionPlayerPanel().update();
                App.getGamePanel().repaint();
                return;
            }
        }
        throw new ConstructBuildingException();
    }

    public void networkBuildCity() {
        TileVertex cVertex = null;
        if (board.isLookingForVertex()) {
            cVertex = board.getClosestTileVertex();
            System.out.println("finding closest vertex");
        }
        if (Main.hasServer()) {
            if (cVertex != null) {
                System.out.println("Closest vertex different from null");
                if (board.canPlaceCity(cVertex, getCurrentPlayer())) {
                    System.out.println("CanPlace City and placing");
                    try {
                        int id = playerClient.getId();
                        NetworkObject object = new NetworkObject(TypeObject.Board, "buildCity",
                                id, cVertex.getId());
                        playerClient.getOut().writeUnshared(object);
                        playerClient.getOut().flush();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        } else {
            if (cVertex != null) {
                if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
                    try {
                        buildCity(cVertex.getId());
                        App.getActionPlayerPanel().update();
                        App.getGamePanel().repaint();
                    } catch (ConstructBuildingException e) {
                        ConstructBuildingException.messageError();
                    }
                }
            }
        }
        // rajouter un if ça a marché (transformer Player.buildCity en boolean)

    }

    /**
     * buildCity
     * @param idVertex the id of the vertex where the player wants to build a city
     * @throws ConstructBuildingException if the player can't build a city on the vertex
     * @see ConstructBuildingException
     * @see Player.buildCity
     * @see TileVertex
     */
    public void buildCity(int idVertex) throws ConstructBuildingException {
        for (TileVertex vertex : board.getVertices()) {
            if (vertex.getId() == idVertex) {
                System.out.println("yeah !");
                board.setLookingForVertex(false);
                board.setPlacingCity(false);
                getCurrentPlayer().buildCity(vertex);
                return;
            }
        }
        throw new ConstructBuildingException();
    }

    public void initialiseGameAfterTransfer(App app) {
        this.app = app;
        app.setBoard(board);
        board.initialiseBoardAfterTransfer();

        if (Main.hasServer()) {
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
            updatePlayerColor();
        }

    }

    public void setPlayerClient(PlayerClient player) {
        playerClient = player;
    }

    public boolean isMyTurn() {
        return playerClient.isMyTurn(this);
    }

    public void updatePlayerColor() {
        for (Player player : players) {
            player.setColor(Player.getColorId(player.getId()));
        }
    }
}
