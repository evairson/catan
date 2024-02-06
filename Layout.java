import java.util.ArrayList;
public class Layout {
    private Orientation orientation;
    private Point origin;
    private Point size;

    public Layout(Orientation orientation, Point origin, Point size) {
        this.orientation = orientation;
        this.origin = origin;
        this.size = size;
    }

    public Point CubeToPixel(Layout layout, CubeCoordinates h) {
        Orientation M = layout.orientation;
        double x = (M.f0 *h.getQ() + M.f1 * h.getR()) * layout.size.getX();
        double y = (M.f2 * h.getQ() + M.f3 * h.getR()) * layout.size.getY();
        return new Point(x + layout.origin.getX(), y + layout.origin.getY());
    }

    public FractionalCubeCoordinates PixelToFractionalCubeCo(Layout layout, Point p) {
        Orientation M = layout.orientation;
        Point pt = new Point((p.getX() - layout.origin.getX()) / layout.size.getX(), (p.getY() - layout.origin.getY()) / layout.size.getY());
        double q = M.b0 * pt.getX() + M.b1 * pt.getY();
        double r = M.b2 * pt.getX() + M.b3 * pt.getY();
        return new FractionalCubeCoordinates(q, r, -q - r);
    }

    public Point CubeCoCornerOffset(Layout layout, int corner) {
        Point size = layout.size;
        double angle = 2.0 * Math.PI * (layout.orientation.startAngle - corner) / 6;
        return new Point(size.getX() * Math.cos(angle), size.getY() * Math.sin(angle));
    }

    public ArrayList<Point> PolygonCorners(Layout layout, CubeCoordinates h) {
        ArrayList<Point> corners = new ArrayList<Point>();
        Point center = CubeToPixel(layout, h);
        for (int i = 0; i < 6; i++) {
            Point offset = CubeCoCornerOffset(layout, i);
            corners.add(new Point(center.getX() + offset.getX(), center.getY() + offset.getY()));
        }
        return corners;
    }
}
