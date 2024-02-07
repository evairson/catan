package model.buildings;

import model.Player;
import model.buildings.Building;
import model.resources.Resources;

import java.awt.*;
import java.util.ArrayList;

public class Road extends Building {

    Point p1;
    Point p2;

    ArrayList<Resources> cost; //dans l'ordre encore ^^^^^^

    public Road(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean buyAndPlace(Player player, Point p1, Point p2){
        if(super.buy(player, cost)){
            Road road = new Road(p1, p2);
            player.getBuildings().add(road);
            return true;
        }
        return false;
    }
}