package model.tiles;

import model.geometry.CubeCoordinates;

public class Tile {
    private int q;
    private int r;
    private int diceValue;
    private int resourceType;

    public Tile(int q, int r) {
        this.q = q;
        this.r = r;
        this.diceValue = 0;
    }

    public Tile(int q, int r, int diceValue) {
        this.q = q;
        this.r = r;
        this.diceValue = diceValue;
    }

    public Tile(int q, int r, int diceValue, int resourceType) {
        this.q = q;
        this.r = r;
        this.diceValue = diceValue;
        this.resourceType = resourceType;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
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

    public CubeCoordinates getCoordinates() {
        return new CubeCoordinates(q, r, -q - r);
    }

    public boolean equals(Tile t) {
        return (t.getQ() == q && t.getR() == r);
    }

    public String toString() {
        return "(" + q + ", " + r + ")";
    }
}
