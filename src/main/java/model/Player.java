package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.buildings.*;
import model.cards.KnightCard;
import model.cards.VictoryPointCard;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import view.TileType;
import model.cards.CardStack;
import model.cards.DevelopmentCard;

public class Player implements Serializable {
    static final int NUMBER_DICE = 6;
    static final int NUMBER_D20 = 20;
    private static final long serialVersionUID = 1L;

    public enum Color {
        RED,
        YELLOW,
        BLUE,
        GREEN,
    }

    public static Color getColorId(int i) {
        switch (i) {
            case 0:
                return Color.RED;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            default:
                return Color.RED;
        }
    }

    protected int id;
    protected Color color;
    protected Boolean turn;
    protected int dice1;
    protected int dice2;
    protected int d20;
    protected String name;
    protected Boolean hasThrowDices;
    protected HashMap<TileType, Integer> resources;
    protected ArrayList<DevelopmentCard> cardsDev;
    protected ArrayList<Building> buildings;

    protected int freeRoad = 0;
    protected Boolean freeColony = true;

    protected int points;
    protected int knights;
    protected boolean hasBiggestArmy;
    protected boolean hasLongestRoute;
    protected int resourceCap;
    protected App app;

    public Player(Color c, String name, int id) {
        this.id = id;
        color = c;
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

    public Player(Color c, String name) {
        color = c;
        this.name = name;
        resources = new HashMap<>();
        resources.put(TileType.CLAY, 6);
        resources.put(TileType.ORE, 6);
        resources.put(TileType.WHEAT, 6);
        resources.put(TileType.WOOD, 6);
        resources.put(TileType.WOOL, 6);
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
    public int getD20() {
        return d20;
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

    public int getId() {
        return id;
    }

    /**
     * This function adds an amount of resource to the specified resource type.
     * @param resourceType The resource type we want to increase
     * @param valueToAdd The increase amount
     */

    public void addResource(TileType resourceType, int valueToAdd) {
        resources.merge(resourceType, valueToAdd, Integer::sum);
    }

    /**
     * Remvoes all resource from specified type.
     * @param resourceType Type of the resource
     */
    public void removeAllResource(TileType resourceType) {
        resources.merge(resourceType, -resources.get(resourceType), Integer::sum);
    }

// ------------------------------------


    public void throwDice1() {
        dice1 = (int) ((Math.random() * NUMBER_DICE) + 1); // (max-min+1)*min
    }

    public void throwDice2() {
        dice2 = (int) ((Math.random() * NUMBER_DICE) + 1);
    }
    public void throwD20() {
        d20 = (int) ((Math.random() * NUMBER_D20) + 1);
    }


    public void throwDices(boolean d20Activated) {
        throwDice1();
        throwDice2();

        if (d20Activated) {
            throwD20();
        }
    }

    public void setDices(int dice1, int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
    }
    public void setDices(int dice1, int dice2, int d20) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.d20 = d20;
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
        App.getGamePanel().repaint();
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
                App.checkWin();
            }
        }
        App.getGamePanel().repaint();
    }

    public void buildCity(TileVertex vertex) {
        if (vertex.getBuilding() != null && vertex.getBuilding() instanceof Colony) {
            if (vertex.getBuilding().getOwner().equals(this)) {
                Colony c = (Colony) vertex.getBuilding();
                if (c.buyAndPlace(this, true, vertex)) {
                    points++;
                    App.checkWin();
                }
            }
        }
        App.getGamePanel().repaint();
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
                App.checkWin();
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
    public void addOneRandom(){
        Random rd = new Random();
        TileType[] resTList = {TileType.CLAY, TileType.ORE, TileType.WHEAT, TileType.WOOD, TileType.WOOL};
        int k = rd.nextInt(0, 5);
        addResource(resTList[k], 1);
    }

    public void incrementKnights(){
        knights++;
    }

    public int getKnights() {
        return knights;
    }
    public void addOnePoint(){
        points++;
    }

    public boolean last(Game game) {
        return (game.getPlayers().get(game.getPlayers().size() - 1) == this);
    }
    public boolean first(Game game) {
        return game.getPlayers().get(0) == this;
    }
}
