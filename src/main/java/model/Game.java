package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import model.buildings.Building;
import model.buildings.Colony;
import model.cards.CardStack;
import model.cards.DevelopmentCard;
import model.tiles.Tile;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import network.NetworkObject;
import network.PlayerClient;
import network.NetworkObject.TypeObject;
import others.Constants;
import others.ListPlayers;
import start.Main;
import view.TileType;

import java.io.Serializable;
import java.util.HashSet;

import exceptionclass.ConstructBuildingException;
import view.gamepanels.TradePanel;

public class Game implements StateMethods, Serializable {
    private GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;
    private boolean resourcesGiven;
    private PlayerClient playerClient;
    private boolean start = true;
    private boolean backwards = false;
    private boolean changeOrder = false;
    private boolean eventOrderJustChanged = false;
    private App app;
    private boolean blankTurn = false;
    private boolean monoWaiting = false;
    private int yearOfPlentyWaiting = 0;
    private Player first;
    private int turnsBeforeHarbourActivated = 0;
    private int turnsBeforeTilesRespawn = 0;
    private ArrayList<TileType> betPot = new ArrayList<>();
    private int tradeEventTurn = 0;

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
        checkForHarboursDisabled();
        checkForHexesRespawn();
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
        } else if (eventOrderJustChanged) {
            eventOrderJustChanged = false;
        } else if (backwards || changeOrder) {
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
        if (app.hasD20()) {
            activateD20Event();
        }
    }

    public void divideRessourcesByTwo() {
        ListPlayers pChecks = (ListPlayers) players.clone();
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

    public void activateD20Event() {
        if (getCurrentPlayer().hasThrowDices()) {
            switch (getCurrentPlayer().getD20()) {
                case 1: killAllSheep(); break;
                case 2 : showDevCards(); break;
                case 3: christmas(); break;
                case 4: lootThiefResources(); break;
                case 5: swapHands(); break;
                case 6 : everyoneThrowsOne(); break;
                case 7: disableHarbour(); break;
                case 8: eventChangeDiceValues(); break;
                case 9: knightLoots(); break;
                case 10: eventChangeOrder(); break;
                case 11: tilesDispawn(); break;
                case 12: tradeAlea(); break;
                case 13: capitalismPoorGetsPoorer(); break;
                case 14: worstWinVP(); break;
                case 15: wildfire(); break;
                case 16: taxCollector(); break;
                case 17: happyBirthday(); break;
                case 18 : everyoneBetsOne(); break;
                case 19 : lootBets(); break;
                case 20: diceSecondRound(); break;
                default:
                    System.out.println("Event non pris en charge");
            }
            App.getActionPlayerPanel().getLogChat().addEventLog(getCurrentPlayer().getD20());
            App.getActionPlayer().getResourcesPanel().updateResourceLabels(getCurrentPlayer());
        }
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
        return (Constants.BuildingCosts.canBuildCity(getCurrentPlayer().getResources())) && resourcesGiven;
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
        return ((Constants.BuildingCosts.canBuildColony(getCurrentPlayer().getResources())) && resourcesGiven)
                || getCurrentPlayer().getFreeColony();
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
        return ((Constants.BuildingCosts.canBuildRoad(getCurrentPlayer().getResources())) && resourcesGiven)
                || getCurrentPlayer().getFreeRoad() > 0;
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
        TileVertex cVertex;
        if (board.isLookingForVertex()) {
            cVertex = board.getClosestTileVertex();
            if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
                getCurrentPlayer().buildColony(cVertex);
                if (Main.hasServer()) {
                    try {
                        int id = playerClient.getId();
                        NetworkObject object = new NetworkObject(TypeObject.Board, "buildColony",
                                id, cVertex.getId());
                        playerClient.getOut().writeUnshared(object);
                        playerClient.getOut().flush();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
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
    }


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
        }
        if (Main.hasServer()) {
            if (cVertex != null) {
                if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
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
    }

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

    public void checkIfTradeEventActive() {
        if (tradeEventTurn > 0) {
            tradeEventTurn--;
        } else {
            TradePanel.setTradeAlea(false);
        }
    }

    // EVENTS DE JEU POUR LE D20

    //event 1
    public void killAllSheep() {
        for (Player player : players) {
            player.removeAllResource(TileType.WOOL);
        }
    }

    //event 2
    public void showDevCards() {
        for (Player player : players) {
            for (DevelopmentCard card : player.getCardsDev()) {
                String message = player.getName() + " a " + card.getName();
                (App.getActionPlayer().getLogChat()).addMessageColor(message, player.getColorAwt());
            }
        }
    }

    //event 3
    public void christmas() {
        for (Player p : players) {
            p.addOneRandom();
            p.addOneRandom();
        }
    }

    //event 4
    public void lootThiefResources() {
        for (Player player : players) {
            for (Building b : player.getBuildings()) {
                if (b instanceof Colony colony) {
                    for (Tile tile : colony.getVertex().getTiles()) {
                        if (tile == thief.getTile()) {
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
        App.getActionPlayerPanel().update();
    }

    //event 5
    public void swapHands() {
        ListPlayers pChecks = (ListPlayers) players.clone();
        Player bestPlayer = pChecks.getFirst();
        Player worstPlayer = pChecks.getFirst();
        for (Player p : pChecks) {
            if (p.getPoints() > bestPlayer.getPoints()) {
                bestPlayer = p;
            }
            if (p.getPoints() < worstPlayer.getPoints()) {
                worstPlayer = p;
            }
        }
        if (bestPlayer != worstPlayer && bestPlayer.getPoints() != worstPlayer.getPoints()) {
            bestPlayer.swapResources(worstPlayer);
        }
    }

    //event 6
    public void everyoneThrowsOne() {
        for (Player p : players) {
            p.removeOneRandom();
        }
    }

    //event 7
    public void disableHarbour() {
        App.getActionPlayer().setHarboursDisabled(true);
        turnsBeforeHarbourActivated = 2 * players.size();
    }
    //Fonction auxiliaire pour gérer la désactivation des ports
    public void checkForHarboursDisabled() {
        if (turnsBeforeHarbourActivated == 0) {
            App.getActionPlayer().setHarboursDisabled(false);
        } else if (turnsBeforeHarbourActivated > 0) {
            turnsBeforeHarbourActivated--;
        }
    }

    //event 8
    public void eventChangeDiceValues() {
        board.eventChangeDiceValues();
    }

    //event 9
    public void knightLoots() {
        for (Player p : players) {
            for (int i = 0; i < p.getKnights(); ++i) {
                p.addOneRandom();
            }
        }
    }

    //event 10
    public void eventChangeOrder() {
        changeOrder = !changeOrder;
        eventOrderJustChanged = true;
    }
    //event 11
    public void tilesDispawn() {
        app.getBoard().setShadowHexes(true);
        turnsBeforeTilesRespawn = 2 * players.size();
    }

    //Fonction auxiliaire pour gérer la désactivation des ports
    public void checkForHexesRespawn() {
        if (turnsBeforeHarbourActivated == 0) {
            app.getBoard().setShadowHexes(false);
        } else if (turnsBeforeHarbourActivated > 0) {
            turnsBeforeHarbourActivated--;
        }
    }

    //event 12
    public void tradeAlea() {
        TradePanel.setTradeAlea(true);
        tradeEventTurn = 2 * players.size();
    }

    //event 13
    public void capitalismPoorGetsPoorer() {
        ListPlayers pChecks = (ListPlayers) players.clone();
        for (Player p : pChecks) {
            System.out.println("Ressources sum : " + p.getResourcesSum());
            if (p.getResourcesSum() <= p.getResourceCap()) {
                for (int i = 0; i < p.getResourcesSum() / 2; i++) {
                    p.removeOneRandom();
                }
            }
        }
    }

    /**
     * Event 14
     * Fait en sorte que le joueur de la partie avec le moins de points gagne 1PV.
     */
    public void worstWinVP() {
        Player min = getCurrentPlayer();
        for (Player p : players) {
            if (p.getPoints() == min.getPoints() && p != min) {
                return;
            } else if (p.getPoints() < min.getPoints()) {
                min = p;
            }
        }
        min.addOnePoint();
        App.checkWin();
    }

    //event 15
    public void wildfire() {
        for (Player p : players) {
            p.removeAllResource(TileType.WOOD);
            p.removeAllResource(TileType.WHEAT);
        }
    }

    //event 16
    public void taxCollector() {
        ListPlayers pChecks = (ListPlayers) players.clone();
        for (Player p : pChecks) {
            int nbColAndCity = p.getNbCitiesAndColonies();
            if (nbColAndCity >= p.getResourcesSum()) {
                p.clearResources();
            } else {
                for (int i = 0; i < nbColAndCity; i++) {
                    p.removeOneRandom();
                }
            }
        }
    }
    //event 17
    public void happyBirthday() {
        ListPlayers pChecks = (ListPlayers) players.clone();
        pChecks.remove(getCurrentPlayer());
        for (Player p : pChecks) {
            p.giftOneRandomResource(getCurrentPlayer());
        }
    }

    //event 18
    public void everyoneBetsOne() {
        for (Player p : players) {
            betPot.add(p.removeOneRandom());
        }
    }

    //event 19
    public void lootBets() {
        for (TileType t : betPot) {
            getCurrentPlayer().addResource(t, 1);
        }
    }

    //event 20
    public void diceSecondRound() {
        resourcesGiven = false;
        lootResources();
    }
}
