package model;

import model.tiles.Tile;

public class Thief {
    private Tile tile;
    private boolean onDesert;

    Thief() {
        onDesert = true;
    }

    public void setOnDesert(boolean b) {
        onDesert = b;
    }

    public boolean isOnDesert() {
        return onDesert;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }


}
