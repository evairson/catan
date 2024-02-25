package model.cards;

import view.TileType;

public class ResourceCard extends Card {
    private final TileType resource;

    public ResourceCard(String name, String description, TileType resource) {
        super(name, description);
        this.resource = resource;
    }

    public TileType getResource() {
        return resource;
    }
}
