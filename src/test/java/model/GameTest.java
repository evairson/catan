package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Game;
import model.Player;
import model.buildings.Road;
import model.geometry.Point;
import model.tiles.TileEdge;

import java.util.HashMap;
import java.util.ArrayList;

public class GameTest {

    @Test
    public void getNumberRoadTest() {
        Player player1 = new Player(Player.Color.BLUE, "null", 1);
        TileEdge edge1 = new TileEdge(new Point(1, 2), new Point(2, 3));
        TileEdge edge2 = new TileEdge(new Point(2, 3), new  Point(3, 4));

        TileEdge edge3 = new TileEdge(new Point(3, 4), new Point(4, 5));

        edge1.setBuilding(new Road(player1));
        edge2.setBuilding(new Road(player1));
        edge3.setBuilding(new Road(player1));

        ArrayList<TileEdge> edgesList = new ArrayList<>();
        edgesList.add(edge1);
        edgesList.add(edge2);
        edgesList.add(edge3);

        assertEquals(3, Game.getNumberRoads(edgesList, edge1, 1));
        assertEquals(2, Game.getNumberRoads(edgesList, edge2, 1));
        assertEquals(1, Game.getNumberRoads(edgesList, edge3, 1));
    }


    @Test
    public void getRoadMaxTest() {
        Player player1 = new Player(Player.Color.BLUE, "null", 1);
        TileEdge edge1 = new TileEdge(new Point(1, 2), new Point(2, 3));
        TileEdge edge2 = new TileEdge(new Point(2, 3), new  Point(3, 4));

        TileEdge edge3 = new TileEdge(new Point(3, 4), new Point(4, 5));

        edge1.setBuilding(new Road(player1));
        edge2.setBuilding(new Road(player1));
        edge3.setBuilding(new Road(player1));

        ArrayList<TileEdge> edgesList = new ArrayList<>();
        edgesList.add(edge1);
        edgesList.add(edge2);
        edgesList.add(edge3);
        assertEquals(edge3, Game.getRoadMax(edgesList, edge3, 1));
        assertEquals(edge3, Game.getRoadMax(edgesList, edge2, 1));
        assertEquals(edge3, Game.getRoadMax(edgesList, edge1, 1));
    }

    @Test
    public void getBestBeforeRoadTest() {
        Player player1 = new Player(Player.Color.BLUE, "null", 1);
        TileEdge edge1 = new TileEdge(new Point(1, 2), new Point(2, 3));
        TileEdge edge2 = new TileEdge(new Point(2, 3), new  Point(3, 4));

        TileEdge edge3 = new TileEdge(new Point(3, 4), new Point(4, 5));

        edge1.setBuilding(new Road(player1));
        edge2.setBuilding(new Road(player1));
        edge3.setBuilding(new Road(player1));
        HashMap<Point, TileEdge> edges = new HashMap<>();
        edges.put(new Point(1.5, 2.5), edge1);
        edges.put(new Point(2.5, 3.5), edge2);
        edges.put(new Point(3.5, 4.5), edge3);

        Game game = new Game(edges);
        assertEquals(edge1, game.getBestBeforeRoad(1));
    }
}
