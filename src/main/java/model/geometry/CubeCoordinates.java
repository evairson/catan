package model.geometry;

import java.io.Serializable;

import others.Constants;

/**
 * CubeCoordinates
 *This class represents the cube coordinates of a hexagon in a hexagonal grid.
    *The cube coordinates are a 3D coordinate system where each hexagon is represented by 3 coordinates (q, r, s).
    *The conventions are q for the x-axis, r for the y-axis, and s for the z-axis.
    *The sum of the three coordinates must be 0.
    *The cube coordinates are used to represent the position of a hexagon in a hexagonal grid.
    *The cube coordinates are used to:
    *   calculate the distance between two hexagons in a hexagonal grid.
    *   calculate the direction of a hexagon in a hexagonal grid.
    *   calculate the neighbor of a hexagon in a hexagonal grid.
    *   calculate the length of a hexagon in a hexagonal grid.
    *   calculate the addition, subtraction, and multiplication of two cube coordinates.
    *   calculate the equality of two cube coordinates.
    */
public class CubeCoordinates implements Serializable {
    private int q;
    private int r;
    private int s;
    
    /**
     * Constructor
     * @param q
     * @param r
     * @param s
     * @throws IllegalArgumentException if q + r + s != 0
     * @return CubeCoordinates with the given coordinates
     */
    public CubeCoordinates(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException("q + r + s must be 0");
        }
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     * @param obj, 
     * @return boolean true if the given object is equal to this cube coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CubeCoordinates)) {
            return false;
        }
        CubeCoordinates c = (CubeCoordinates) obj;
        return (c.q == this.q && c.r == this.r && c.s == this.s);
    }


    /**
     * @return int the hash code of this cube coordinates
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + q;
        result = 31 * result + r;
        result = 31 * result + s;
        return result;
    }
    /**
     * @param b
     * @return CubeCoordinates the addition of this cube coordinates and the given cube coordinates
     */
    public CubeCoordinates add(CubeCoordinates b) {
        return new CubeCoordinates(this.q + b.q, this.r + b.r, this.s + b.s);
    }

    /**
     * @param b
     * @return CubeCoordinates the subtraction of this cube coordinates and the given cube coordinates
     */
    public CubeCoordinates subtract(CubeCoordinates b) {
        return new CubeCoordinates(this.q - b.q, this.r - b.r, this.s - b.s);
    }

    /**
     * @param k
     * @return CubeCoordinates the multiplication of this cube coordinates by the given number
     */
    public CubeCoordinates multiply(int k) {
        return new CubeCoordinates(this.q * k, this.r * k, this.s * k);
    }

    /**
     * @return int the length of this cube coordinates
     */
    public int length() {
        return (Math.abs(this.q) + Math.abs(this.r) + Math.abs(this.s)) / 2;
    }

    /**
     * @param b
     * @return int the distance between this cube coordinates and the given cube coordinates
     */
    public int distance(CubeCoordinates b) {
        return this.subtract(b).length();
    }

    /**
     * @param direction
     * 
     * The directions represent the 6 directions of a hexagon in a hexagonal grid.
     * The directions are represented by cube coordinates.
     * The directions are used to calculate the neighbor of a hexagon in a hexagonal grid.
     * 
     * @return CubeCoordinates the direction of this cube coordinates in the given direction
     */
    public CubeCoordinates direction(int direction/* 0 to 5 */) {
        if (direction < 0 || direction > 5) {
            throw new IllegalArgumentException("Direction must be between 0 and 5");
        }
        return Constants.CubeCoordinatesConst.DIRECTIONS[direction];
    }


    public CubeCoordinates neighbor(int direction/* 0 to 5 */) {
        if (direction < 0 || direction > 5) {
            throw new IllegalArgumentException("Direction must be between 0 and 5");
        }
        return this.add(direction(direction));
    }

    /**
     * @return int return the s
     */
    public int getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(int s) {
        this.s = s;
    }

    /**
     * @return int return the r
     */
    public int getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(int r) {
        this.r = r;
    }

    /**
     * @return int return the q
     */
    public int getQ() {
        return q;
    }

    /**
     * @param q the q to set
     */
    public void setQ(int q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "(q: " + this.q + " r: " + this.r + " s: " + this.s + ")";
    }
}
