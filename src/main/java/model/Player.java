package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    private Boolean hasThrowDices = false;
    private HashMap<TileType, Integer> resources;
    private ArrayList<DevelopmentCard> cardsDev;
    private ArrayList<Building> buildings;
    private int freeRoad = 0;
    private Boolean freeColony = true;

    private int points;
    private boolean hasBiggestArmy;
    private boolean hasLongestRoute;
    private int resourceCap;
    private App app;

    public Player(Color c, String name, App app) {
        color = c;
        this.app = app;
        this.name = name;
        resources = new HashMap<>();
        resources.put(TileType.CLAY, 0);
        resources.put(TileType.ORE, 0);
        resources.put(TileType.WHEAT, 0);
        resources.put(TileType.WOOD, 0);
        resources.put(TileType.WOOL, 0);
        buildings = new ArrayList<>();
        cardsDev = new ArrayList<>();
        hasThrowDices = false;
        points = 0;
        hasBiggestArmy = false;
        hasLongestRoute = false;
        resourceCap = 7;
    }

    public void printBuildings() {
        for (Building b : buildings) {
            System.out.println(b);
        }
    }

    public int getFreeRoad() {
        return freeRoad;
    }

    public void setFreeRoad(int i) {
        freeRoad = i;
    }

    public boolean getFreeColony() {
        return freeColony;
    }

    public void setFreeColony(boolean b) {
        freeColony = b;
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

    public int getResourceCap() {
        return resourceCap;
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

    public int getPoints() {
        return points;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<Colony> getColony() {
        ArrayList<Colony> array = new ArrayList<>();
        for (Building b : getBuildings()) {
            if (b instanceof Colony) {
                array.add((Colony) b);
            }
        }
        return array;
    }

    public ArrayList<Road> getRoads() {
        ArrayList<Road> array = new ArrayList<>();
        for (Building b : getBuildings()) {
            if (b instanceof Road) {
                array.add((Road) b);
            }
        }
        return array;
    }

    public HashMap<TileType, Integer> getResources() {
        return resources;
    }
    public int getResource(TileType t) {
        return resources.get(t);
    }
    public int getResourcesSum() {
        int acc = 0;
        for (Integer i : resources.values()) {
            acc += i;
        }
        return acc;
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
            if (freeRoad > 0) {
                r.place(this, edge);
                freeRoad--;
                return;
            }
            r.buyAndPlace(this, edge);
        }
    }

    public void buildColony(TileVertex vertex) {
        if (vertex.getBuilding() == null) {
            Colony c = new Colony(this);
            if (freeColony) {
                setFreeColony(false);
                freeRoad++;
                c.place(this, false, vertex);
                return;
            }
            if (c.buyAndPlace(this, false, vertex)) {
                points++;
                app.checkWin();
            }
        }
    }

    public void buildCity(TileVertex vertex) {
        if (vertex.getBuilding() != null && vertex.getBuilding() instanceof Colony) {
            if (vertex.getBuilding().getOwner().equals(this)) {
                Colony c = (Colony) vertex.getBuilding();
                if (c.buyAndPlace(this, true, vertex)) {
                    points++;
                    app.checkWin();
                }
            }
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
                app.checkWin();
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
    public void removeOneRandom() {
        if (getResourcesSum() > 0) {
            Random rd = new Random();
            TileType[] resTList = {TileType.CLAY, TileType.ORE, TileType.WHEAT, TileType.WOOD, TileType.WOOL};
            while (true) {
                int k = rd.nextInt(0, 5);
                if (resources.get(resTList[k]) > 0) {
                    addResource(resTList[k], -1);
                    break;
                }
            }
        }
    }
}
