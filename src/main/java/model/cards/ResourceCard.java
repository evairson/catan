package model.cards;

import model.resources.Resources;

public class ResourceCard extends Card {
    private Resources resource;

    public ResourceCard(String name, String description, Resources resource) {
        super(name, description);
        this.resource = resource;
    }

    public Resources getResource() {
        return resource;
    }
}
