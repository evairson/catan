package model;

import java.util.ArrayList;

import model.buildings.*;
import model.resources.*;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import model.buildings.Building;
import model.cards.CardStack;
import model.cards.DevelopmentCard;
import model.resources.Resources;

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
        resources.add(new Clay(1));
        resources.add(new Ore(8));
        resources.add(new Wheat(8));
        resources.add(new Wood(3));
        resources.add(new Wool(3));
        buildings = new ArrayList<>();
        cardsDev = new ArrayList<>();
    }

    public void printBuildings() {
        for (Building b : buildings) {
            System.out.println(b);
        }
    }

    public boolean hasColony() {
        for (Building b : buildings) {
            if (b instanceof Colony) {
                return true;
            }
        }
        return false;
    }

    public void printResources() {
        for (Resources r : resources) {
            System.out.print(r + " ");
        }
        System.out.println();
    }

    // Getter / Setter : ---------------

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
            case GREEN:
                return "Green";
            case BLUE:
                return "Blue";
            case RED:
                return "Red";
            case YELLOW:
                return "Yellow";
            default:
                return "none";
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

    public void placeBuilding(TileVertex vertex) {
        // TODO :
    }

    public void buildRoad(TileEdge edge) {
        if (edge.getBuilding() == null) {
            Road r = new Road(this);
            r.buyAndPlace(this, edge);
            System.out.println("Road built");
        } else {
            System.out.println("Road not built");
        }
    }

    public void buildColony(TileVertex vertex) {
        if (vertex.getBuilding() == null) {
            Colony c = new Colony(this);
            if (c.buyAndPlace(this, false, vertex)) {
                System.out.println("Colony built");
            }
        } else {
            System.out.println("Colony not built");
        }
    }

    public void buildCity(TileVertex vertex) {
        if (vertex.getBuilding() != null && vertex.getBuilding() instanceof Colony) {
            if (vertex.getBuilding().getOwner().equals(this)) {
                Colony c = (Colony) vertex.getBuilding();
                if (c.buyAndPlace(this, true, vertex)) {
                    System.out.println("City built");
                }
            }
        } else {
            System.out.println("City not built");
        }
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
