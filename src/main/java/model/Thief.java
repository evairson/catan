package model;

import java.io.Serializable;
import model.tiles.Tile;
import view.TileType;

public class Thief implements Serializable {
    private Tile tile;
    private boolean onDesert;

    public boolean isOnDesert() {
        return tile.getResourceType() == TileType.DESERT;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }


}
