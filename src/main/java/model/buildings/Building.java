package model.buildings;

import model.Player;
import model.resources.Resources;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Building implements Serializable {
    private Player owner;

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

