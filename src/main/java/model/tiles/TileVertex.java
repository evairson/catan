package model.tiles;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

public class TileVertex implements Serializable {
    private Set<Tile> tiles;
    private Building building;
    private int id;

    public TileVertex() {
        tiles = new HashSet<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
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
