package model.buildings;

import model.Player;
import others.Constants;
import model.tiles.TileVertex;

public class Colony extends Building {
    private boolean isCity = false;
    private boolean isPort = false;
    private TileVertex vertex;

    public Colony(Player owner) {
        super(owner);
    }

    public Colony(Player player, boolean isCity, boolean isPort, TileVertex vertex) {
        super(player);
        this.isCity = isCity;
        this.isPort = isPort;
        this.vertex = vertex;
    }

    public boolean getIsCity() {
        return isCity;
    }

    public void setVille() {
        isCity = true;
    }

    public TileVertex getVertex() {
        return vertex;
    }

    public boolean cityBuyable(Player player) {
        int[] cost = getCost(true);
        return super.buyable(player, cost);
    }

    public boolean colonyBuyable(Player player) {
        int[] cost = getCost(false);
        return super.buyable(player, cost);
    }

    public void setPort() {
        isPort = true;
    }

    public int getPoint() {
        return (isCity ? 2 : 1);
    }

    public int[] getCost(boolean isCity) {
        if (isCity) {
            return Constants.BuildingCosts.CITY;
        }
        return Constants.BuildingCosts.COLONY;
    }

    public boolean buyAndPlace(Player player, boolean isCity, TileVertex vertex) {
        if (super.buy(player, getCost(isCity))) {
            Colony colony = new Colony(player, isCity, false, vertex);
            player.getBuildings().remove(vertex.getBuilding());
            vertex.setBuilding(colony);
            // faudrait check si c'est un port aussi là
            player.getBuildings().add(colony);
            System.out.println(colony);
            return true;
        }
        return false;
    }
}
