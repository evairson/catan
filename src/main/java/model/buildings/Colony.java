package model.buildings;

import model.Player;
import model.resources.Resources;

import java.awt.*;
import java.util.ArrayList;

public class Colony extends Building {
    private boolean isCity = false;
    private boolean isPort = false;

    private Point p;

    private ArrayList<Resources> cost_colony; //dans l'ordre
    private ArrayList<Resources> cost_city;

    public Colony(Point p) {
        this.p = p;
    }

    public Colony(Point p, Boolean isCity, boolean isPort) {
        this(p);
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
            return cost_city;
        }
        return cost_colony;
    }

    public boolean buy(Player player, Point p, boolean isCity) {
        if (super.buy(player, getCost(isCity))) {
            Colony colony = new Colony(p, isCity, false);
            //faudrait check si c'est un port aussi là
            player.getBuildings().add(colony);
            return true;
        }
        return false;
    }
}

