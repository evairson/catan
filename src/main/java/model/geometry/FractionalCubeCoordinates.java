package model.geometry;

import java.io.Serializable;

/**
 * FractionalCubeCoordinates
 * This class represents the fractional cube coordinates of a hexagon in a hexagonal grid.
 * The fractional cube coordinates are used to represent relative positions
 * inside a hexagon rather than the center of the hexagon.
 * The fractional cube coordinates are used to:
 * calculate the distance between two hexagons in a hexagonal grid.
 * calculate the closest neighbour of a hexagon in a hexagonal grid.
 * calculate the two closest neighbours of a hexagon in a hexagonal grid.
 * calculate the addition, subtraction, and multiplication of two fractional cube coordinates.
 * calculate the equality of two fractional cube coordinates.
 */
public class FractionalCubeCoordinates implements Serializable {
    private double q;
    private double r;
    private double s;

    /**
     * Creates FractionalCubeCoordinates with the given coordinates.
     * @param q
     * @param r
     * @param s
     */
    public FractionalCubeCoordinates(double q, double r, double s) {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    // Getters and Setters
    /**
     * @return double q
     */
    public double getQ() {
        return this.q;
    }

    /**
     * @return double r
     */
    public double getR() {
        return this.r;
    }

    /**
     * @return double s
     */
    public double getS() {
        return this.s;
    }

    /**
     * @param q
     * sets the q coordinate
     */
    public void setQ(double q) {
        this.q = q;
    }

    /**
     * @param r
     * sets the r coordinate
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * @param s
     * sets the s coordinate
     */
    public void setS(double s) {
        this.s = s;
    }

    // Methods

    /**
     * Rounds the fractional cube coordinates to the nearest cube coordinates
     * This method is used to find inside which hexagon the fractional cube coordinates are located.
     * @return CubeCoordinates the cube coordinates of the rounded fractional cube coordinates
     */
    public CubeCoordinates round() {
        int q = (int) Math.round(this.q);
        int r = (int) Math.round(this.r);
        int s = (int) Math.round(this.s);
        double qDiff = Math.abs(q - this.q);
        double rDiff = Math.abs(r - this.r);
        double sDiff = Math.abs(s - this.s);
        if (qDiff > rDiff && qDiff > sDiff) {
            q = -r - s;
        } else if (rDiff > sDiff) {
            r = -q - s;
        } else {
            s = -q - r;
        }
        return new CubeCoordinates(q, r, s);
    }

    /**
     * @return FractionalCubeCoordinates the distance between this
     * fractional cube coordinates and the given fractional cube coordinates
     */
    public double distance(FractionalCubeCoordinates b) {
        return (Math.abs(this.q - b.q) + Math.abs(this.r - b.r) + Math.abs(this.s - b.s)) / 2;
    }

    /**
     * findClosestNeighbour
     * This method is used to find the closest neighbour of a hexagon in a hexagonal grid.
     * The closest neighbour is the neighbour that has the smallest distance to the hexagon.
     * More specifically, the closest neighbour is the neighbour that has the smallest distance to the position inside the hexagon.
     * Used to pinpoint the common edge of two hexes when placing roads.
     * @return CubeCoordinates the closest neighbour of the fractional cube coordinates
     */
    public CubeCoordinates findClosestNeighbour() {
        CubeCoordinates rounded = this.round();
        FractionalCubeCoordinates[] neighbours = new FractionalCubeCoordinates[6];
        neighbours[0] = new FractionalCubeCoordinates(rounded.getQ() + 1,
                rounded.getR() - 1, rounded.getS());
        neighbours[1] = new FractionalCubeCoordinates(rounded.getQ() + 1,
                rounded.getR(), rounded.getS() - 1);
        neighbours[2] = new FractionalCubeCoordinates(rounded.getQ(),
                rounded.getR() + 1, rounded.getS() - 1);
        neighbours[3] = new FractionalCubeCoordinates(rounded.getQ() - 1,
                rounded.getR() + 1, rounded.getS());
        neighbours[4] = new FractionalCubeCoordinates(rounded.getQ() - 1,
                rounded.getR(), rounded.getS() + 1);
        neighbours[5] = new FractionalCubeCoordinates(rounded.getQ(),
                rounded.getR() - 1, rounded.getS() + 1);

        double minDistance = Double.MAX_VALUE;
        CubeCoordinates closestNeighbour = new CubeCoordinates(0, 0, 0);
        for (int i = 0; i < 6; i++) {
            double distance = this.distance(neighbours[i]);
            if (distance < minDistance) {
                minDistance = distance;
                closestNeighbour = neighbours[i].round();
            }
        }
        return closestNeighbour;
    }

    /**
     * findTwoClosestNeighbours
     * This method is used to find the two closest neighbours of a hexagon in a hexagonal grid.
     * The two closest neighbours are the neighbours that have the smallest distance to the hexagon.
     * More specifically, the two closest neighbours are the neighbours that have the smallest distance to the position inside the hexagon.
     * Used to pinpoint on which vertex of the hexagon we place cities by comparing the common vertex of three hexes. 
     * @return CubeCoordinates[] the two closest neighbours of the fractional cube coordinates
     */
    public CubeCoordinates[] findTwoClosestNeighbours() {
        CubeCoordinates rounded = this.round();
        FractionalCubeCoordinates[] neighbours = new FractionalCubeCoordinates[6];
        neighbours[0] = new FractionalCubeCoordinates(rounded.getQ() + 1,
                rounded.getR() - 1, rounded.getS());
        neighbours[1] = new FractionalCubeCoordinates(rounded.getQ() + 1,
                rounded.getR(), rounded.getS() - 1);
        neighbours[2] = new FractionalCubeCoordinates(rounded.getQ(),
                rounded.getR() + 1, rounded.getS() - 1);
        neighbours[3] = new FractionalCubeCoordinates(rounded.getQ() - 1,
                rounded.getR() + 1, rounded.getS());
        neighbours[4] = new FractionalCubeCoordinates(rounded.getQ() - 1,
                rounded.getR(), rounded.getS() + 1);
        neighbours[5] = new FractionalCubeCoordinates(rounded.getQ(),
                rounded.getR() - 1, rounded.getS() + 1);

        double minDistance = Double.MAX_VALUE;
        CubeCoordinates closestNeighbour = new CubeCoordinates(0, 0, 0);
        CubeCoordinates secondClosestNeighbour = new CubeCoordinates(0, 0, 0);
        for (int i = 0; i < 6; i++) {
            double distance = this.distance(neighbours[i]);
            if (distance < minDistance) {
                minDistance = distance;
                secondClosestNeighbour = closestNeighbour;
                closestNeighbour = neighbours[i].round();
            }
        }
        CubeCoordinates[] result = {closestNeighbour, secondClosestNeighbour};
        return result;
    }
}
