package model.tiles;

import model.geometry.Point;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import model.Game;
import model.buildings.Building;

/**
 * TileEdge
 * This class represents an edge of a tile in a hexagonal grid.
 * A tile edge is a line in the grid. it represents the edge of a tile in the game.
 * it has a start and end point, a set of tiles that are connected to the edge, and a building.
 * the building placed on the edge can only be a road.
 */
public class TileEdge implements Serializable {
    private Point start;
    private Point end;
    private Set<Tile> tiles;
    private Building building;
    private static int idClass;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getIdClass() {
        return idClass;
    }

    public static void addIdClass() {
        idClass++;
    }

    /**
     * Constructor with no parameters
     * the start and end points are set to (0, 0).
     * the set of tiles is initialized.
     * The set contains the tiles that are connected to the edge.
     * It is used for testing purposes and for initialization purposes.
     */
    public TileEdge() {
        start = new Point(0, 0);
        end = new Point(0, 0);
        tiles = new HashSet<>();
    }

    /**
     * Constructor with start and end points
     * @param start
     * @param end
     * the set of tiles is initialized.
     * The set contains the tiles that are connected to the edge.
     * We don't give the building as a parameter because the edge does not contain a default building at the beginning.
     */
    public TileEdge(Point start, Point end) {
        this.start = start;
        this.end = end;
        tiles = new HashSet<>();
    }

    /**
     * Constructor with start and end points and a set of tiles
     * @param start
     * @param end
     * @param tiles
     * the set of tiles is initialized.
     * The set contains the tiles that are connected to the edge.
     * We don't give the building as a parameter because the edge does not contain a default building at the beginning.

     */
    public TileEdge(Point start, Point end, Set<Tile> tiles) {
        this.start = start;
        this.end = end;
        this.tiles = tiles;
    }

    // getters and setters
    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Building getBuilding() {
        return building;
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
        tile.addEdge(this);
    }

    public void setBuilding(Building building) {
        this.building = building;
        System.out.println("Building set for TileEdge");
    }

    public int getNumberEdgesBefore(Game game) {
        int number = 0;
        for (TileEdge edge : game.getBoard().getEdgeMap().values()) {
            if (edge == this) {
                continue;
            }
            if (edge.getStart().getX() == end.getX() && edge.getStart().getY() == end.getY()
                || edge.getEnd().getX() == end.getX() && edge.getEnd().getY() == end.getY()) {
                if (edge.getBuilding() != null) {
                    number++;
                }
            }
        }
        return number;
    }

    public static void resetIdClass() {
        idClass = 0;
    }
}
