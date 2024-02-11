package model.geometry;

import java.util.ArrayList;

public class Layout {
    private Orientation orientation;
    private Point origin;
    private Point size;

    public Layout(Orientation orientation, Point point, Point point2) {
        this.orientation = orientation;
        this.origin = point;
        this.size = point2;
    }

    public Point cubeToPixel(Layout layout, CubeCoordinates h) {
        Orientation m = layout.orientation;
        double x = (m.getF0() * h.getQ() + m.getF1() * h.getR()) * layout.size.getX();
        double y = (m.getF2() * h.getQ() + m.getF3() * h.getR()) * layout.size.getY();
        return new Point(x + layout.origin.getX(), y + layout.origin.getY());
    }

    public Point fractionalCubeToPixel(Layout layout, FractionalCubeCoordinates h) {
        return cubeToPixel(layout, h.round());
    }

    public FractionalCubeCoordinates pixelToFractionalCubeCo(Layout layout, Point p) {
        Orientation m = layout.orientation;
        Point pt = new Point((p.getX() - layout.origin.getX()) / layout.size.getX(),
                (p.getY() - layout.origin.getY()) / layout.size.getY());
        double q = m.getB0() * pt.getX() + m.getB1() * pt.getY();
        double r = m.getB2() * pt.getX() + m.getB3() * pt.getY();
        return new FractionalCubeCoordinates(q, r, -q - r);
    }

    public Point cubeCoCornerOffset(Layout layout, int corner) {
        Point size = layout.size;
        double angle = 2.0 * Math.PI * (layout.orientation.getStartAngle() - corner) / 6;
        return new Point(size.getX() * Math.cos(angle), size.getY() * Math.sin(angle));
    }

    public ArrayList<Point> polygonCorners(Layout layout, CubeCoordinates h) {
        ArrayList<Point> corners = new ArrayList<Point>();
        Point center = cubeToPixel(layout, h);
        for (int i = 0; i < 6; i++) {
            Point offset = cubeCoCornerOffset(layout, i);
            corners.add(new Point(center.getX() + offset.getX(), center.getY() + offset.getY()));
        }
        return corners;
    }

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
