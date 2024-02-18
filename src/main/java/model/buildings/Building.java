package model.buildings;

import model.Player;
import model.resources.Resources;

import java.util.ArrayList;
import java.awt.*;

public abstract class Building {
    private Player owner;

    public Building(Player owner) {
        this.owner = owner;
    }

    public boolean buyable(Player player, ArrayList<Resources> cost) {
        for (Resources value : cost) {
            for (Resources resource : player.getResources()) {
                if (value.getClass() != resource.getClass()) {
                    continue;
                }
                if (value.getAmount() <= resource.getAmount()) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if (this instanceof Road) {
            return ((Road) this).getEdge().getStart() + " // " + ((Road) this).getEdge().getEnd();
        }
            else return "";
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

    public boolean buy(Player player, ArrayList<Resources> cost) {
        if (buyable(player, cost)) {
            for (int i = 0; i < cost.size(); i++) {
                player.getResources().get(i).payAmount(cost.get(i).getAmount());
                return true;
            }
        }
        return false;
    }
}
