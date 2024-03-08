package model.tiles;

import java.io.Serializable;
import model.geometry.Point;
import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

public class TileVertex implements Serializable {
    private Set<Tile> tiles;
    private Building building;
    private static int idClass;
    private int id;
    private Point coordinates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public TileVertex() {
        tiles = new HashSet<>();
    }

    public static int getIdClass() {
        return idClass;
    }

    public static void addIdClass() {
        idClass++;
    }

    public static void resetIdClass() {
        idClass = 0;
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
