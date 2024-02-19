package model.buildings;

import model.Player;
import model.resources.Resources;
import model.tiles.Tile;

import java.awt.*;
import java.util.ArrayList;

public class Colony extends Building {
    private boolean isCity = false;
    private boolean isPort = false;

    private Point p;

    private ArrayList<Resources> costColony; //dans l'ordre
    private ArrayList<Resources> costCity;

    private ArrayList<Tile> tiles;

    public Colony(Point p) {
        this.p = p;
    }

    public Colony(Point p, Boolean isCity, boolean isPort, ArrayList<Tile> tiles) {
        this(p);
        this.isCity = isCity;
        this.isPort = isPort;
        this.tiles = tiles;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
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

    public boolean buy(Player player, Point p, boolean isCity) {
        if (super.buy(player, getCost(isCity))) {
            Colony colony = new Colony(p, isCity, false, null);
            //ajouter les tiles à proximité
            //faudrait check si c'est un port aussi là
            player.getBuildings().add(colony);
            return true;
        }
        return false;
    }
}

