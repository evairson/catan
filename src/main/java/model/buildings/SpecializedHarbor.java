package model.buildings;

import model.tiles.TileVertex;
import view.TileType;

public class SpecializedHarbor extends Harbor {
    private final TileType resourceType;
    public SpecializedHarbor(TileVertex location, TileType resourceType) {
        super(location, 2);
        this.resourceType = resourceType;
    }
    public TileType getResourceType() {
        return resourceType;
    }
}
