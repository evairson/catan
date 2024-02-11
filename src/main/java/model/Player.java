package model;

import java.util.ArrayList;

import model.buildings.Building;
import model.resources.Resources;

public class Player {
    static final int NUMBER_DICE = 7;

    public enum Color {
        RED,
        WHITE,
        BLUE,
        ORANGE
    }

    private Color color;
    private Boolean turn;
    private int dice1;
    private int dice2;
    private String name;
    // private Coordonnee cord;
    private ArrayList<Resources> resources;


    // private ArrayList<Card> cardsDev;
    private ArrayList<Building> buildings;

    public Player(Color c, String name) {
        color = c;
        this.name = name;
        resources = new ArrayList<>();
        buildings = new ArrayList<>();
    }

// Getter / Setter :  ---------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    public Boolean isTurn() {
        return turn;
    }

    public void setTurn(Boolean b) {
        turn = b;
    }

    public int getDies() {
        return dice1 + dice2;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }

// ------------------------------------

    public void throwDice1() {
        dice1 = (int) (Math.random() * NUMBER_DICE); // (max-min+1)*min
    }

    public void throwDice2() {
        dice2 = (int) (Math.random() * NUMBER_DICE);
    }

    public void throwDices() {
        throwDice1();
        throwDice2();
    }

    public void placeBuilding() {
        // TODO :
    }

    public void createOrBuy() {
        // TODO :
    }

    public void changeWith(Player p) {
        // TODO :
    }

}
