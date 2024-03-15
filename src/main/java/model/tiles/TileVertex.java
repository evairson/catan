package model.tiles;

import model.buildings.Harbor;
import model.geometry.Point;
import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

public class TileVertex {

    private Set<Tile> tiles;
    private Building building;
    private Harbor harbor;
    private Point coordinates;
    public TileVertex() {
        tiles = new HashSet<>();
    }

    public void setHarbor(Harbor harbor) {
        this.harbor = harbor;
    }

    public Harbor getHarbor() {
        return harbor;
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
