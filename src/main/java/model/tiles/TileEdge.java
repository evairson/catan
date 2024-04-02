package model.tiles;

import model.geometry.Point;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import model.Game;
import model.buildings.Building;

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

    // make a constructor for default
    public TileEdge() {
        start = new Point(0, 0);
        end = new Point(0, 0);
        tiles = new HashSet<>();
    }

    public TileEdge(Point start, Point end) {
        this.start = start;
        this.end = end;
        tiles = new HashSet<>();
    }

    public TileEdge(Point start, Point end, Set<Tile> tiles) {
        this.start = start;
        this.end = end;
        this.tiles = tiles;
    }

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
