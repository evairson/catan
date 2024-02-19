package model;

import model.tiles.Tile;

public class Thief {
    private Tile tile;
    private boolean onDesert;

    public boolean isOnDesert() {
        return tile.getResourceType() == 0;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }


}
