package model.tiles;

import model.geometry.Point;

import java.util.HashSet;
import java.util.Set;

public class TileEdge {
    private Point start;
    private Point end;
    private Set<Tile> tiles;

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

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }
}
