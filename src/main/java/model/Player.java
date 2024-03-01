package model;

import java.util.ArrayList;
import java.util.HashMap;

import model.buildings.*;
import model.cards.VictoryPointCard;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import view.TileType;
import model.buildings.Building;
import model.cards.CardStack;
import model.cards.DevelopmentCard;

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
    private HashMap<TileType, Integer> resources;
    private ArrayList<DevelopmentCard> cardsDev;
    private ArrayList<Building> buildings;
    private int points;
    private boolean hasBiggestArmy;
    private boolean hasLongestRoute;
    public Player(Color c, String name) {
        color = c;
        this.name = name;
        resources = new HashMap<>();
        resources.put(TileType.CLAY, 1);
        resources.put(TileType.ORE, 8);
        resources.put(TileType.WHEAT, 8);
        resources.put(TileType.WOOD, 3);
        resources.put(TileType.WOOL, 3);
        buildings = new ArrayList<>();
        cardsDev = new ArrayList<>();
        hasThrowDices = false;
        points = 0;
        hasBiggestArmy = false;
        hasLongestRoute = false;
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
        for (TileType r : resources.keySet()) {
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

    public HashMap<TileType, Integer> getResources() {
        return resources;
    }
    public ArrayList<DevelopmentCard> getCardsDev() {
        return cardsDev;
    }
    public boolean hasWon() {
        return points >= 10;
    }
    public boolean hasBiggestArmy() {
        return hasBiggestArmy;
    }
    public boolean hasLongestRoute() {
        return hasLongestRoute;
    }

    public void setCardsDev(ArrayList<DevelopmentCard> cardsDev) {
        this.cardsDev = cardsDev;
    }

    /**
     * This function adds an amount of resource to the specified resource type.
     * @param resourceType The resource type we want to increase
     * @param valueToAdd The increase amount
     */

    public void addResource(TileType resourceType, int valueToAdd) {
        resources.merge(resourceType, valueToAdd, Integer::sum);
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
            points++;
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
                points++;
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
        if (!stack.getCardStack().isEmpty()) {
            DevelopmentCard card = stack.getCardStack().pop();
            if (card instanceof VictoryPointCard) {
                points++;
            }
            cardsDev.add(card);
        } else {
            System.out.println("0 cartes dans le deck");
        }
    }

    /**
     * This method checks if we have enough amounts of resources to match those of the resourcesToGive array.
     * @param resourcesToGive The amounts of the resources we want to give
     * @return true if we have enough, false if we don't
     */
    public boolean hasEnough(int[] resourcesToGive) {
        int[] ourResources = {resources.get(TileType.CLAY),
                resources.get(TileType.ORE),
                resources.get(TileType.WHEAT),
                resources.get(TileType.WOOD),
                resources.get(TileType.WOOL)};
        for (int i = 0; i < 5; i++) {
            if (resourcesToGive[i] > ourResources[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method adds the amounts of resources contained in the resourcesAmountsToAdd array.
     * @param resourceAmountsToAdd The amounts of resources we add
     */
    public void addResourceAmount(int[] resourceAmountsToAdd) {
        addResource(TileType.CLAY, resourceAmountsToAdd[0]);
        addResource(TileType.ORE, resourceAmountsToAdd[1]);
        addResource(TileType.WHEAT, resourceAmountsToAdd[2]);
        addResource(TileType.WOOD, resourceAmountsToAdd[3]);
        addResource(TileType.WOOL, resourceAmountsToAdd[4]);
    }

    /**
     * This method removes the amount of resources contained in the resourcesAmountsToRemove array.
     * @param resourceAmountsToRemove The amounts of resources we remove
     */
    public void removeResourceAmount(int[] resourceAmountsToRemove) {
        addResource(TileType.CLAY, -resourceAmountsToRemove[0]);
        addResource(TileType.ORE, -resourceAmountsToRemove[1]);
        addResource(TileType.WHEAT, -resourceAmountsToRemove[2]);
        addResource(TileType.WOOD, -resourceAmountsToRemove[3]);
        addResource(TileType.WOOL, -resourceAmountsToRemove[4]);
    }
}
