package model.buildings;

import model.App;
import model.Player;
import others.Constants;
import java.awt.*;
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

    public static int[] getCost() {
        return Constants.BuildingCosts.ROAD;
    }
    public boolean buyAndPlace(Player player, TileEdge edge) {
        if (buy(player)) {
            place(player, edge);
            App.getGamePanel().repaint();
            return true;
        }
        return false;
    }

    public boolean buy(Player player) {
        return super.buy(player, getCost());
    }

    public void place(Player player, TileEdge edge) {
        Road road = new Road(player, edge, player.getColor());
        player.getBuildings().add(road);
        edge.setBuilding(road);
    }
}
