package model.tiles;

import java.util.HashSet;
import java.util.Set;

public class TileVertex {
    private Set<Tile> tiles;

    public TileVertex() {
        tiles = new HashSet<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public Set<Tile> getTiles() {
        return tiles;
    }
}