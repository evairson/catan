package model.geometry;

import java.io.Serializable;

/**
 * Point
 * This class represents a point in a 2D plane.
 * The point is used to determine pixel coordinates on the screen mostly.
 */
public class Point implements Serializable {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean equals(Point p) {
        return this.x == p.x && this.y == p.y;
    }
}
