package model.buildings;

import model.Player;
import model.resources.Resources;

import java.awt.*;
import java.util.ArrayList;

public class Colony extends Building {
    private boolean isCity = false;
    private boolean isPort = false;


    private ArrayList<Resources> costColony; //dans l'ordre
    private ArrayList<Resources> costCity;

    public Colony(Player owner) {
        super(owner);

    }

    public Colony(Player player, Boolean isCity, boolean isPort) {
        super(player);
        this.isCity = isCity;
        this.isPort = isPort;
    }

    public void setVille() {
        isCity = true;
    }

    public void setPort() {
        isPort = true;
    }

    public int getPoint() {
        return (isCity ? 2 : 1);
    }

    public ArrayList<Resources> getCost(boolean isCity) {
        if (isCity) {
            return costCity;
        }
        return costColony;
    }

    public boolean buy(Player player, boolean isCity) {
        if (super.buy(player, getCost(isCity))) {
            Colony colony = new Colony(player, isCity, false);
            //faudrait check si c'est un port aussi là
            player.getBuildings().add(colony);
            return true;
        }
        return false;
    }
}

