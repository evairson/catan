package model.buildings;

import model.tiles.TileVertex;

public class Harbor {
    private TileVertex location;

    public Harbor(TileVertex location) {
        this.location = location;
    }

    public TileVertex getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Harbor{" + "location=" + location.getCoordinates() + '}';
    }
}
