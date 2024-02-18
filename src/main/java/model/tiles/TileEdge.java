package model.tiles;

import model.geometry.Point;

import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

public class TileEdge {

    private Point start;
    private Point end;
    private Set<Tile> tiles;
    private Building building;

    // make a constructor for default
    public TileEdge() {
        start = new Point(0, 0);
        end = new Point(0, 0);
        tiles = new HashSet<>();
        System.out.println("TileEdge created");

    }

    public TileEdge(Point start, Point end) {
        this.start = start;
        this.end = end;
        tiles = new HashSet<>();
        System.out.println("TileEdge created");
    }

    public TileEdge(Point start, Point end, Set<Tile> tiles) {
        this.start = start;
        this.end = end;
        this.tiles = tiles;
        System.out.println("TileEdge created");
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
    }

    public void setBuilding(Building building) {
        this.building = building;
        System.out.println("Building set for TileEdge");
    }
}
