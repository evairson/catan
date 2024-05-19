package model.geometry;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Layout
 * This class represents the layout of a hexagonal grid.
 * The layout is used to convert between pixel coordinates and cube coordinates.
 * The layout is used to:
 * calculate the pixel coordinates of a hexagon in a hexagonal grid.
 * calculate the fractional cube coordinates of a hexagon in a hexagonal grid.
 * calculate the cube coordinates of a pixel in a hexagonal grid.
 * calculate the corner offset of a hexagon in a hexagonal grid.
 *      The corner offset is used to calculate the corners of a hexagon in a hexagonal grid.
 * calculate the corners of a hexagon in a hexagonal grid.
 *      The corners are used to draw a hexagon in a hexagonal grid.
 */
public class Layout implements Serializable {
    private Orientation orientation;
    private Point origin;
    private Point size;

    /**
     * Constructor.
     * @param orientation
     * @param point
     * @param point2
     */
    public Layout(Orientation orientation, Point point, Point point2) {
        this.orientation = orientation;
        this.origin = point;
        this.size = point2;
    }

    /**
     * cubeToPixel.
     * @param layout
     * @param h
     * calculate the pixel coordinates of a the center
     * of a hexagon from cube coordinates, according to the layout
     * @return a Point representing the pixel coordinates of the center of the hexagon
     */
    public Point cubeToPixel(Layout layout, CubeCoordinates h) {
        Orientation m = layout.orientation;
        double x = (m.getF0() * h.getQ() + m.getF1() * h.getR()) * layout.size.getX();
        double y = (m.getF2() * h.getQ() + m.getF3() * h.getR()) * layout.size.getY();
        return new Point(x + layout.origin.getX(), y + layout.origin.getY());
    }

    /**
     * fractionalCubeToPixel.
     * @param layout
     * @param h
     * calculate the pixel coordinates of a the center of
     * a hexagon from fractional cube coordinates, according to the layout
     * this basically rounds the fractional cube coordinates to
     * the nearest cube coordinates and then calculates the pixel coordinates
     * using the previous method @see cubeToPixel
     * @return a Point representing the pixel coordinates of the center of the hexagon
     */
    public Point fractionalCubeToPixel(Layout layout, FractionalCubeCoordinates h) {
        return cubeToPixel(layout, h.round());
    }

    /**
     * pixelToFractionalCubeCo.
     * @param layout
     * @param p
     * calculate the fractional cube coordinates of a hexagon from pixel coordinates, according to the layout
     * @return FractionalCubeCoordinates representing the position inside the hexagon.
     * It is usually used to calculate the position of the mouse for example.
     */
    public FractionalCubeCoordinates pixelToFractionalCubeCo(Layout layout, Point p) {
        Orientation m = layout.orientation;
        Point pt = new Point((p.getX() - layout.origin.getX()) / layout.size.getX(),
                (p.getY() - layout.origin.getY()) / layout.size.getY());
        double q = m.getB0() * pt.getX() + m.getB1() * pt.getY();
        double r = m.getB2() * pt.getX() + m.getB3() * pt.getY();
        return new FractionalCubeCoordinates(q, r, -q - r);
    }

    /**
     * cubeCoCornerOffset.
     * @param layout
     * @param corner
     * calculate the corner offset of a hexagon in a hexagonal grid.
     * an offset is a distance from a point to another point.
     * The corner offset is used to calculate the positions of the corners of a hexagon in a hexagonal grid.
     * @return a Point representing the corner offset of the hexagon
     */
    public Point cubeCoCornerOffset(Layout layout, int corner) {
        Point size = layout.size;
        double angle = 2.0 * Math.PI * (layout.orientation.getStartAngle() - corner) / 6;
        return new Point(size.getX() * Math.cos(angle), size.getY() * Math.sin(angle));
    }

    /**
     * polygonCorners.
     * @param layout
     * @param h (CubeCoordinates)
     * calculate the corners of a hexagon in a hexagonal grid.
     * The corners are used to draw a hexagon in a hexagonal grid.
     * @return an ArrayList of Points representing the corners of the hexagon
     * using this and the previous method @see cubeCoCornerOffset
     * you can draw the hexagon using the corners and the distance between the corners
     */
    public ArrayList<Point> polygonCorners(Layout layout, CubeCoordinates h) {
        ArrayList<Point> corners = new ArrayList<Point>();
        Point center = cubeToPixel(layout, h);
        for (int i = 0; i < 6; i++) {
            Point offset = cubeCoCornerOffset(layout, i);
            corners.add(new Point(center.getX() + offset.getX(), center.getY() + offset.getY()));
        }
        return corners;
    }

    /**
     * polygonCorners //overload//.
     * @param layout
     * @param h (FractionalCubeCoordinates)
     * calculate the corners of a hexagon in a hexagonal grid.
     * The corners are used to draw a hexagon in a hexagonal grid.
     * done by first rounding the fractional cube coordinates to the nearest cube coordinates
     * @return an ArrayList of Points representing the corners of the hexagon
     * using this and the previous method @see cubeCoCornerOffset
     * you can draw the hexagon using the corners and the distance between the corners
     */
    public ArrayList<Point> polygonCorners(Layout layout, FractionalCubeCoordinates h) {
        ArrayList<Point> corners = new ArrayList<Point>();
        Point center = cubeToPixel(layout, h.round());
        for (int i = 0; i < 6; i++) {
            Point offset = cubeCoCornerOffset(layout, i);
            corners.add(new Point(center.getX() + offset.getX(), center.getY() + offset.getY()));
        }
        return corners;
    }
}
