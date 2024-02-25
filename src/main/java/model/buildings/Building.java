package model.buildings;

import model.Player;
import view.TileType;

import java.awt.*;
import java.util.HashMap;

public abstract class Building {
    private Player owner;

    public Building(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean buyable(Player player, int[] cost) {
        for (int i = 0; i < cost.length; i++) {
            if (player.getResources().get(TileType.CLAY) < cost[0]) {
                return false;
            }
            if (player.getResources().get(TileType.ORE) < cost[1]) {
                return false;
            }
            if (player.getResources().get(TileType.WHEAT) < cost[2]) {
                return false;
            }
            if (player.getResources().get(TileType.WOOD) < cost[3]) {
                return false;
            }
            if (player.getResources().get(TileType.WOOL) < cost[4]) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if (this instanceof Road) {
            return ((Road) this).getEdge().getStart() + " // " + ((Road) this).getEdge().getEnd();
        } else {
            return "";
        }
    }

    public Color getColorInAwt() {
        switch (owner.getColor()) {
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.YELLOW;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            default:
                return Color.BLACK;
        }
    }

    public boolean buy(Player player, int[] cost) {
        if (buyable(player, cost)) {
            HashMap<TileType, Integer> resources = player.getResources();

            resources.replace(TileType.CLAY, resources.get(TileType.CLAY) - cost[0]);
            System.out.println("payed " + cost[0] + " clay");

            resources.replace(TileType.ORE, resources.get(TileType.ORE) - cost[1]);
            System.out.println("payed " + cost[1] + " ore");

            resources.replace(TileType.WHEAT, resources.get(TileType.WHEAT) - cost[2]);
            System.out.println("payed " + cost[2] + " wheat");

            resources.replace(TileType.WOOD, resources.get(TileType.WOOD) - cost[3]);
            System.out.println("payed " + cost[3] + " wood");

            resources.replace(TileType.WOOL, resources.get(TileType.WOOL) - cost[4]);
            System.out.println("payed " + cost[4] + " wool");
            return true;
        }
        return false;
    }
}
