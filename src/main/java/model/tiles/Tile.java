package model.tiles;

import java.io.Serializable;

import model.geometry.CubeCoordinates;
import view.TileType;
/**
 * Tile
 * This class represents a tile in a hexagonal grid.
 * A tile is a hexagon in the grid. it represents a resource tile in the game.
 * it has a q and r coordinates, a dice value, and a resource type.
 * The q and r coordinates are the cube coordinates of the tile (q, r, -q-r).
 * The dice value is the value that the dice must have to activate the tile.
 * The resource type is the type of resource that the tile produces.
 * the id is a unique identifier for the tile.
 */
public class Tile implements Serializable {
    private int q;
    private int r;
    private int diceValue;
    private TileType resourceType;
    private int id;
    private static int idClass = 0;

    /**
     * Constructor with q and r coordinates
     * @param q
     * @param r
     * the dice value is set to 0, for the resource type, it is set to null.
     * that is for tiles that are not resource tiles.
     */
    public Tile(int q, int r) {
        this.q = q;
        this.r = r;
        this.diceValue = 0;
        id = idClass;
        idClass++;
    }

    /**
     * Constructor with q, r coordinates and dice value
     * @param q
     * @param r
     * @param diceValue
     * the resource type is set to null.
     * that can be used for tiles such as the desert tile.
     */
    public Tile(int q, int r, int diceValue) {
        this.q = q;
        this.r = r;
        this.diceValue = diceValue;
        id = idClass;
        idClass++;
    }

    /**
     * Constructor with q, r coordinates, dice value and resource type
     * @param q
     * @param r
     * @param diceValue
     * @param resourceType
     * this constructor is used for resource tiles.
     */
    public Tile(int q, int r, int diceValue, TileType resourceType) {
        this.q = q;
        this.r = r;
        this.diceValue = diceValue;
        this.resourceType = resourceType;
        id = idClass;
        idClass++;
    }

    // getters and setters


    public TileType getResourceType() {
        return resourceType;
    }

    public void setResourceType(TileType resourceType) {
        this.resourceType = resourceType;
    }

    public int getDiceValue() {
        return diceValue;
    }

    public void setDiceValue(int diceValue) {
        this.diceValue = diceValue;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    /**
     * getCoordinates
     * @return CubeCoordinates. We use the q and r coordinates to get the s coordinate, and we return the cube coordinates.
     * that is because q + r + s = 0 so we can get the s coordinate by -q - r.
     */
    public CubeCoordinates getCoordinates() {
        return new CubeCoordinates(q, r, -q - r);
    }

    public boolean equals(Tile t) {
        return (t.getQ() == q && t.getR() == r);
    }

    public String toString() {
        return "(" + q + ", " + r + ")";
    }

    public int getId() {
        return id;
    }

    public static void addIdClass() {
        idClass++;
    }

    public static int getIdClass() {
        return idClass;
    }
}
