package model.buildings;

import model.Player;
import model.resources.Resources;

import java.awt.*;
import java.util.ArrayList;
import model.tiles.TileEdge;

public class Road extends Building {

    private TileEdge edge;
    private Player.Color playerColor;
    private ArrayList<Resources> cost; // dans l'ordre encore ^^^^^^

    public Road(Player owner, TileEdge edge, Player.Color playerColor) {
        super(owner);
        this.edge = edge;
    }

    public ArrayList<Resources> getCost() {
        return cost;
    }

    public void setCost(ArrayList<Resources> cost) {
        this.cost = cost;
    }

    public boolean buyAndPlace(Player player, TileEdge edge, ArrayList<Resources> cost) {
        if (super.buy(player, cost)) {
            Road road = new Road(player, edge, player.getColor());
            player.getBuildings().add(road);
            return true;
        }
        return false;
    }
}
