package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.cards.CardStack;
import model.geometry.Layout;
import model.geometry.Point;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import others.Constants;
import others.ListPlayers;

public class Game implements StateMethods {
    private static GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;

    Game() {
        Player player1 = new Player(Player.Color.RED, "Player1");
        Player player2 = new Player(Player.Color.YELLOW, "Player2");
        Player player3 = new Player(Player.Color.BLUE, "Player3");
        Player player4 = new Player(Player.Color.GREEN, "Player4");
        players = new ListPlayers(0, player1, player2, player3, player4);

        Point point1 = new Point(400, 400);
        Point point2 = new Point(50, 50);
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        board = new GameBoard(layout);

        stack = new CardStack();
    }

    public CardStack getStack() {
        return stack;
    }

    public void endTurn() {
        players.next();
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

    public void draw(Graphics g) {
        board.draw(g);
    }


    // Player action : -----------------

    public void mouseMoved(MouseEvent e) {
        board.mouseMoved(e);
    }

    @Override
    public void update() {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
        if (board.isPlacingCity()) {
            buildCity();
            System.out.println("Building city");
        } else if (board.isPlacingColony()) {
            buildColony();
            System.out.println("Building colony");
        } else if (board.isPlacingRoad()) {
            buildRoad();
            System.out.println("Building road");
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

}
