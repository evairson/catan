package model.tiles;

import model.geometry.Point;
import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

public class TileVertex {

    private Set<Tile> tiles;
    private Building building;
    private Point coordinates;
    public TileVertex() {
        tiles = new HashSet<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }
    public Point getCoordinates() {
        return coordinates;
    }
    public void setBuilding(Building building) {
        this.building = building;
    }
    public Building getBuilding() {
        return building;
    }
    public Set<Tile> getTiles() {
        return tiles;
    }
}
