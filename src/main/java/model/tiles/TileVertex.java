package model.tiles;

import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

public class TileVertex {

    private Set<Tile> tiles;
    private Building building;

    public TileVertex() {
        tiles = new HashSet<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Set<Tile> getTiles() {
        return tiles;
    }
}
