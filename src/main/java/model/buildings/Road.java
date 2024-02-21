package model.buildings;

import model.Player;
import model.resources.Resources;
import others.Constants;
import java.awt.*;
import java.util.ArrayList;
import model.tiles.TileEdge;

public class Road extends Building {

    private TileEdge edge;
    private Player.Color playerColor;
    
    public Road(Player owner) {
        super(owner);
    }

    public Road(Player owner, TileEdge edge, Player.Color playerColor) {
        super(owner);
        this.edge = edge;
        this.playerColor = playerColor;
    }
    public boolean buyable(Player player) {
        return super.buyable(player, getCost());
    }

    public TileEdge getEdge() {
        return this.edge;
    }

    public int[] getCost() {
        return Constants.BuildingCosts.ROAD;
    }
    public boolean buyAndPlace(Player player, TileEdge edge) {
        if (super.buy(player, getCost())) {
            Road road = new Road(player, edge, player.getColor());
            player.getBuildings().add(road);
            edge.setBuilding(road);
            return true;
        }
        return false;
    }
}
