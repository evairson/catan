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
import others.Constants;
import others.ListPlayers;
//import view.utilities.Resolution;
import view.TileType;

public class Game implements StateMethods {
    private static GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;
    private boolean resourcesGiven;
    private boolean start = true;
    private boolean backwards = false;
    private App app;
    private boolean blankTurn = false;
    private boolean monoWaiting = false;
    private int yearOfPlentyWaiting = 0;
    private Player first;

    public Game(App app) {
        this.app = app;
        resourcesGiven = false;
        Player player1 = new Player(Player.Color.RED, "Player1", app);
        Player player2 = new Player(Player.Color.YELLOW, "Player2", app);
        Player player3 = new Player(Player.Color.BLUE, "Player3", app);
        Player player4 = new Player(Player.Color.GREEN, "Player4", app);
        players = new ListPlayers(0, player1, player2, player3, player4);
        player1.setFreeColony(true);
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
        app.setBoard(board);
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
        // TODO : Update les ressources du joueur en direct ^^
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

        if (first == null) {
            first = getCurrentPlayer();
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

        if (start && players.peekNext() == first) {
            start = false;
            backwards = true;
        } else if (backwards && getCurrentPlayer() == first) {
            backwards = false;
        } else if (backwards) {
            players.prev();
        } else {
            players.next();
        }

        if (!start && !backwards && !blankTurn) {
            app.getActionPlayerPanel().getRollingDice().setButtonIsOn(true);
        }
        System.out.println("It's " + getCurrentPlayer() .getName() + "'s turn");
        resourcesGiven = false;
        app.getActionPlayerPanel().getRollingDice().newPlayer(getCurrentPlayer());

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

    public static void setBoard(GameBoard board) {
        Game.board = board;
    }

    public void setThiefMode(boolean b) {
        board.setThiefMode(b);
        divideRessourcesByTwo();
    }

    public void draw(Graphics g) {
        board.draw(g);
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
        app.getActionPlayerPanel().update();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (board.isPlacingCity()) {
            buildCity();
        } else if (board.isPlacingColony()) {
            buildColony();
        } else if (board.isPlacingRoad()) {
            buildRoad();
        } else if (board.getThiefMode()) {
            board.changeThief();
            board.setThiefModeEnd(true);
        }
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

    public void buildColony() {
        if (board.isLookingForVertex()) {
            TileVertex cVertex = board.getClosestTileVertex();
            if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
                getCurrentPlayer().buildColony(cVertex);
            }
        }
        // rajouter un if ça a marché (transformer Player.buildColony en boolean)
        board.setLookingForVertex(false);
        board.setPlacingColony(false);
    }

    public void buildRoad() {
        if (board.isLookingForEdge()) {
            TileEdge cEdge = board.getClosestTileEdge();
            if (board.canPlaceRoad(cEdge, getCurrentPlayer())) {
                getCurrentPlayer().buildRoad(cEdge);
            }
        }
        // rajouter un if ça a marché (transformer Player.buildRoad en boolean)
        board.setLookingForEdge(false);
        board.setPlacingRoad(false);
    }

    public void buildCity() {
        if (board.isLookingForVertex()) {
            TileVertex cVertex = board.getClosestTileVertex();
            if (board.canPlaceColony(cVertex, getCurrentPlayer())) {
                getCurrentPlayer().buildCity(cVertex);
            }
        }
        // rajouter un if ça a marché (transformer Player.buildCity en boolean)
        board.setLookingForVertex(false);
        board.setPlacingCity(false);
    }
}
