package model.geometry;

import java.io.Serializable;

/**
 * Orientation
 * This class represents the orientation of a hexagonal grid.
 * The orientation is basically whether the hexagons are pointy or flat.
 * We have two default orientations found in the Constants class.
 */
public class Orientation implements Serializable {

    private double startAngle; // multiple of 60 degrees

    private double f0;
    private double f1;
    private double f2;
    private double f3;

    private double b0;
    private double b1;
    private double b2;
    private double b3;

    /**
     * Constructor.
     * @param startAngle
     * this is a multiple of 60 degrees, to determine where in a circle the hexagon start
     * The following parameters are used to calculate the next angles of the hexagon
     * f0, f1, f2, f3 are used to calculate the forward angles of the hexagon
     * b0, b1, b2, b3 are used to calculate the backward angles of the hexagon
     * @param f0
     * @param f1
     * @param f2
     * @param f3
     * @param b0
     * @param b1
     * @param b2
     * @param b3
     */
    public Orientation(double startAngle, double f0, double f1, double f2, double f3, double b0,
            double b1, double b2, double b3) {
        this.startAngle = startAngle;
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.b0 = b0;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    // ------------------------GETTERS------------------------//
    public double getStartAngle() {
        return this.startAngle;
    }

    public double getF0() {
        return this.f0;
    }

    public double getF1() {
        return this.f1;
    }

    public double getF2() {
        return this.f2;
    }

    public double getF3() {
        return this.f3;
    }

    public double getB0() {
        return this.b0;
    }

    public double getB1() {
        return this.b1;
    }

    public double getB2() {
        return this.b2;
    }

    public double getB3() {
        return this.b3;
    }

    // ------------------------SETTERS------------------------//

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public void setF0(double f0) {
        this.f0 = f0;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public void setF2(double f2) {
        this.f2 = f2;
    }

    public void setF3(double f3) {
        this.f3 = f3;
    }

    public void setB0(double b0) {
        this.b0 = b0;
    }

    public void setB1(double b1) {
        this.b1 = b1;
    }

    public void setB2(double b2) {
        this.b2 = b2;
    }

    public void setB3(double b3) {
        this.b3 = b3;
    }

    // -------------------------------------------------------//

}
