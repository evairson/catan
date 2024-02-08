package model.buildings;

import model.Player;
import model.resources.Resources;

import java.awt.*;
import java.util.ArrayList;

public class Road extends Building {

    private Point p1;
    private Point p2;

    private ArrayList<Resources> cost; //dans l'ordre encore ^^^^^^

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public ArrayList<Resources> getCost() {
        return cost;
    }

    public void setCost(ArrayList<Resources> cost) {
        this.cost = cost;
    }

    public Road(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean buyAndPlace(Player player, Point p1, Point p2) {
        if (super.buy(player, cost)) {
            Road road = new Road(p1, p2);
            player.getBuildings().add(road);
            return true;
        }
        return false;
    }
}
