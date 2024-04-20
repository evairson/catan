package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import model.geometry.*;
import model.IA.Bot;
import model.IA.ThreadBot;
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
import java.util.HashMap;

import exceptionclass.ConstructBuildingException;

public class Game implements StateMethods, Serializable {
    private GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;
    private boolean resourcesGiven;
    private Player playerClient;
    private boolean start = true;
    private boolean backwards = false;
    private App app;
    private boolean blankTurn = false;
    private boolean monoWaiting = false;
    private int yearOfPlentyWaiting = 0;
    private Player first;

    public Game(HashMap<Point, TileEdge> edge) { // Pour les tests
        thief = new Thief();
        board = new GameBoard(edge, this);
        stack = new CardStack();
    }

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

    public boolean isStart() {
        return start;
    }

    public boolean isBackwards() {
        return backwards;
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
                ((PlayerClient) playerClient).getOut().writeUnshared(object);
                ((PlayerClient) playerClient).getOut().flush();
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
        App.getActionPlayerPanel().getResourcesPanel().updateResourceLabels(getCurrentPlayer());
        System.out.println("monopoly de " + amount + " " + t);
    }

    public boolean canPass() {
        Player p = getCurrentPlayer();
        if (p.getFreeRoad() > 0) {
            System.out.println("freeRoad");
            return false;
        }
        if (monoWaiting || yearOfPlentyWaiting > 0) {
            System.out.println("monoWaiting");
            return false;
        }
        if (p.getFreeColony()) {
            System.out.println("freeColony");
            return false;
        }
        if (!p.hasThrowDices() && !start && !backwards) {
            System.out.println("hasthrowdice");
            return false;
        }
        if (board.getThiefMode()) {
            System.out.println("thief");
            return false;
        }
        return true;
    }

    public void endTurn() {
        if (!canPass()) {
            System.out.println("mince");
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
        App.addMessageColor("C'est au tour de ", java.awt.Color.RED);
        App.addMessageColor(app.getGame().getCurrentPlayer().getName() + "\n",
            app.getGame().getCurrentPlayer().getColorAwt());
        App.getGamePanel().repaint();

        if (!Main.hasServer()) {
            startTurnBot();
        }
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

    public Player getPlayerClient() {
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
            System.out.println("Building city");
        } else if (board.isPlacingColony()) {
            networkBuildColony();
            System.out.println("Building colony");
        } else if (board.isPlacingRoad()) {
            networkBuildRoad();
            System.out.println("Building road");
        } else if (board.getThiefMode()) {
            board.changeThiefNetwork();
            board.setThiefModeEnd(true);
        }
        App.getGamePanel().repaint();
        App.getActionPlayerPanel().update();
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

    public boolean canBuildCity() {
        if (blankTurn) {
            return false;
        }
        if (((Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) && resourcesGiven)) {
            return true;
        }
        return false;
    }

    public void buildCityButtonAction() {
        if (blankTurn) {
            return;
        }
        if (((Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) && resourcesGiven)) {
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
                    ((PlayerClient) playerClient).getOut().writeUnshared(object);
                    ((PlayerClient) playerClient).getOut().flush();
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

    public boolean buildColony(int idVertex) throws ConstructBuildingException {
        for (TileVertex vertex : board.getVertices()) {
            if (vertex.getId() == idVertex) {
                if (board.canPlaceColony(vertex, playerClient)) {
                    board.setLookingForVertex(false);
                    board.setPlacingCity(false);
                    if (getCurrentPlayer().buildColony(vertex)) {
                        App.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                            app.getGame().getCurrentPlayer().getColorAwt());
                        App.addMessageColor(" vient de placer une colonie \n", java.awt.Color.RED);
                        App.getActionPlayerPanel().update();
                        App.getGamePanel().repaint();
                        return true;
                    }
                    return false;
                }
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
                        ((PlayerClient) playerClient).getOut().writeUnshared(object);
                        ((PlayerClient) playerClient).getOut().flush();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        } else {
            if (cEdge != null) {
                try {
                    buildRoad(cEdge.getId());
                } catch (ConstructBuildingException e) {
                    ConstructBuildingException.messageError();
                }
            }
        }
        // rajouter un if ça a marché (transformer Player.buildRoad en boolean)
        board.setLookingForEdge(false);
        board.setPlacingRoad(false);
    }

    public boolean buildRoad(int idEdge) throws ConstructBuildingException {
        for (TileEdge edge : board.getEdgeMap().values()) {
            if (edge.getId() == idEdge) {
                board.setLookingForVertex(false);
                board.setPlacingCity(false);
                if (getCurrentPlayer().buildRoad(edge)) {
                    App.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                        app.getGame().getCurrentPlayer().getColorAwt());
                    App.addMessageColor(" vient de placer une route \n", java.awt.Color.RED);
                    App.getActionPlayerPanel().update();
                    App.getGamePanel().repaint();
                    return true;
                } else {
                    return false;
                }
            }
        }
        throw new ConstructBuildingException();
    }

    public void networkBuildCity() {
        TileVertex cVertex = null;
        if (board.isLookingForVertex()) {
            cVertex = board.getClosestTileVertex();
        }
        if (Main.hasServer()) {
            if (cVertex != null) {
                if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
                    try {
                        int id = playerClient.getId();
                        NetworkObject object = new NetworkObject(TypeObject.Board, "buildCity",
                                id, cVertex.getId());
                        ((PlayerClient) playerClient).getOut().writeUnshared(object);
                        ((PlayerClient) playerClient).getOut().flush();
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

    public Boolean buildCity(int idVertex) throws ConstructBuildingException {
        for (TileVertex vertex : board.getVertices()) {
            if (vertex.getId() == idVertex) {
                System.out.println("yeah !");
                board.setLookingForVertex(false);
                board.setPlacingCity(false);
                if (getCurrentPlayer().buildCity(vertex)) {
                    App.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                        app.getGame().getCurrentPlayer().getColorAwt());
                    App.addMessageColor(" vient de placer une ville \n", java.awt.Color.RED);
                    App.getActionPlayerPanel().update();
                    App.getGamePanel().repaint();
                    return true;
                }
                return false;
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

    public void setPlayerClient(Player player) {
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


    // Fonction lié aux Bots :

    public void startTurnBot() {
        if (getCurrentPlayer() instanceof Bot) {
            ThreadBot threadBot = new ThreadBot(this, (Bot) getCurrentPlayer());
            threadBot.start();
        }
    }

    public void placeRoadAndColonyBot(boolean road) {
        boolean hasPlaced = false;
        while (!hasPlaced) {
            int nbAlea = (int) (Math.random() * (road ? board.getEdgeMap().size()
                : board.getVertices().size()));
            try {
                if (!road) {
                    if (buildColony(nbAlea)) {
                        hasPlaced = true;
                    }
                } else {
                    if (buildRoad(nbAlea)) {
                        hasPlaced = true;
                    }
                }
            } catch (ConstructBuildingException e) {
                ConstructBuildingException.messageError();
            }
        }
        App.getGamePanel().repaint();
    }

    // Route la plus longue

    public static int getNumberRoads(ArrayList<TileEdge> edges, TileEdge edge, int idPlayer) {
        ArrayList<TileEdge> edgesNext = new ArrayList<>();
        ArrayList<TileEdge> edgesCopy = (ArrayList<TileEdge>) edges.clone();
        for (TileEdge edgeNext : edges) {
            if (edgeNext == edge) {
                continue;
            }
            if (edgeNext.getEnd().distance(edge.getEnd()) == 0
                || edgeNext.getStart().distance(edge.getEnd()) == 0) {
                if (edgeNext.getBuilding() != null && edgeNext.getBuilding().getOwner().getId() == idPlayer) {
                    edgesNext.add(edgeNext);
                    edgesCopy.remove(edgeNext);
                }
            }
        }
        if (edgesNext.size() == 0) {
            return 1;
        } else if (edgesNext.size() == 1) {
            return 1 + getNumberRoads(edgesCopy, edgesNext.get(0), idPlayer);
        } else {
            return 1 + Math.max(getNumberRoads(edges, edgesNext.get(0), idPlayer),
                getNumberRoads(edgesCopy, edgesNext.get(1), idPlayer));
        }
    }

    /**
     * @param edges : Edge de départ
     * @param edge : Edge de départ
     * @param idPlayer : L'id du player voulu
     * @return edge d'arrivé du plus long chemin
     */
    public static TileEdge getRoadMax(ArrayList<TileEdge> edges, TileEdge edge, int idPlayer) {
        ArrayList<TileEdge> edgesNext = new ArrayList<>();
        ArrayList<TileEdge> edgesCopy = (ArrayList<TileEdge>) edges.clone();
        for (TileEdge edgeNext : edges) {
            if (edgeNext == edge) {
                continue;
            }
            if (edgeNext.getEnd().distance(edge.getEnd()) == 0
                || edgeNext.getStart().distance(edge.getEnd()) == 0) {
                if (edgeNext.getBuilding() != null && edgeNext.getBuilding().getOwner().getId() == idPlayer) {
                    edgesNext.add(edgeNext);
                    edgesCopy.remove(edgeNext);
                }
            }
        }
        if (edgesNext.size() == 0) {
            System.out.println("c'est zero");
            return edge;
        } else if (edgesNext.size() == 1) {
            System.out.println("1");
            return getRoadMax(edgesCopy, edgesNext.get(0), idPlayer);
        } else {
            System.out.println("2");
            if (getNumberRoads(edgesCopy, edgesNext.get(0), idPlayer)
                > getNumberRoads(edgesCopy, edgesNext.get(1), idPlayer)) {
                return getRoadMax(edgesCopy, edgesNext.get(0), idPlayer);
            } else {
                return getRoadMax(edgesCopy, edgesNext.get(1), idPlayer);
            }
        }
    }

    public TileEdge getBestBeforeRoad(int id) {
        int numbersRoadsMax = 0;
        TileEdge edgeFirstMax = null;
        ArrayList<TileEdge> edges = new ArrayList<>();
        for (TileEdge edge: board.getEdgeMap().values()) {
            edges.add(edge);
        }
        for (TileEdge edge: board.getEdgeMap().values()) {
            if (edge.getBuilding() != null && edge.getBuilding().getOwner().getId() == id) {
                if (edgeFirstMax == null) {
                    edgeFirstMax = edge;
                }
                int numberRoads = Game.getNumberRoads(edges, edge, id);
                if (numberRoads > numbersRoadsMax) {
                    numbersRoadsMax = numberRoads;
                    edgeFirstMax = edge;
                }
            }
        }

        System.out.println(numbersRoadsMax);
        return edgeFirstMax;
    }
}
