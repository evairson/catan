package model.buildings;

import model.tiles.TileVertex;

public class Harbor {
    private TileVertex location;
    private int tradeRatio;

    public Harbor(TileVertex location, int tradeRatio) {
        this.location = location;
        this.tradeRatio = tradeRatio;
    }

    public TileVertex getLocation() {
        return location;
    }
}
