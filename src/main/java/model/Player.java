package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.buildings.Building;
import model.cards.CardStack;
import model.cards.DevelopmentCard;
import model.resources.Resources;

public class Player implements Serializable {
    static final int NUMBER_DICE = 6;

    public enum Color {
        RED,
        YELLOW,
        BLUE,
        GREEN
    }

    protected Color color;
    protected Boolean turn;
    protected int dice1;
    protected int dice2;
    protected String name;
    protected Boolean hasThrowDices;
    protected ArrayList<Resources> resources;


    protected ArrayList<DevelopmentCard> cardsDev;
    protected ArrayList<Building> buildings;

    public Player(Color c, String name) {
        color = c;
        this.name = name;
        resources = new ArrayList<>();
        buildings = new ArrayList<>();
        cardsDev = new ArrayList<>();
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

    public String getColorString() {
        switch (color) {
            case GREEN : return "Green";
            case BLUE : return "Blue";
            case RED : return "Red";
            case YELLOW : return "Yellow";
            default : return "none";
        }
    }

    public void setColor(Color c) {
        color = c;
    }

    public boolean hasThrowDices() {
        return hasThrowDices;
    }

    public void setHasTrowDices(Boolean b) {
        hasThrowDices = b;
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

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<Resources> getResources() {
        return resources;
    }

    public ArrayList<DevelopmentCard> getCardsDev() {
        return cardsDev;
    }

    public void setCardsDev(ArrayList<DevelopmentCard> cardsDev) {
        this.cardsDev = cardsDev;
    }

// ------------------------------------


    public void throwDice1() {
        dice1 = (int) ((Math.random() * NUMBER_DICE) + 1); // (max-min+1)*min
    }

    public void throwDice2() {
        dice2 = (int) ((Math.random() * NUMBER_DICE) + 1);
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

    public void drawCard(CardStack stack) {
        cardsDev.add(stack.getCardStack().pop());
    }

}
