package model;

import java.util.ArrayList;

import model.buildings.Building;
import model.cards.CardStack;
import model.cards.DevelopmentCard;
import model.resources.Clay;
import model.resources.Resources;
import model.resources.Sheep;
import model.resources.Stone;
import model.resources.Wheat;
import model.resources.Wood;

public class Player {
    static final int NUMBER_DICE = 6;

    public enum Color {
        RED,
        YELLOW,
        BLUE,
        GREEN
    }

    private Color color;
    private Boolean turn;
    private int dice1;
    private int dice2;
    private String name;
    private Boolean hasThrowDices;
    private ArrayList<Resources> resources;


    private ArrayList<DevelopmentCard> cardsDev;
    private ArrayList<Building> buildings;

    public Player(Color c, String name) {
        color = c;
        this.name = name;
        resources = new ArrayList<>();
        resources.add(new Wood(0));
        resources.add(new Wheat(0));
        resources.add(new Clay(0));
        resources.add(new Sheep(0));
        resources.add(new Stone(0));
        buildings = new ArrayList<>();
        cardsDev = new ArrayList<>();
        hasThrowDices = false;
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

    public int getDice() {
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
        if (!stack.getCardStack().isEmpty()) {
            cardsDev.add(stack.getCardStack().pop());
        } else {
            System.out.println("0 cartes dans le deck");
        }
    }

}
