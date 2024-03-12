package model.buildings;

import model.tiles.TileVertex;
import view.TileType;

public class SpecializedHarbor extends Harbor {
    private final TileType resourceType;
    public SpecializedHarbor(TileVertex location, TileType resourceType) {
        super(location);
        this.resourceType = resourceType;
    }
    public TileType getResourceType() {
        return resourceType;
    }

    @Override
    public String toString() {
        return "SpecializedHarbor{" + "location="
                + getLocation().getCoordinates() + ", resourceType=" + resourceType + '}';
    }
}
