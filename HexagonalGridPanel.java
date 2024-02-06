import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HexagonalGridPanel extends JPanel {
    private Layout layout;
    private int hoveredHexagonQ = -800;
    private int hoveredHexagonR = -800;

    public HexagonalGridPanel(Layout layout) {
        this.layout = layout;

        // Add mouse listener to track mouse events
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("Mouse moved: " + e.getX() + ", " + e.getY());
                updateHoveredHexagon(new Point(e.getX(), e.getY()));
            }
        });
    }

    private void updateHoveredHexagon(Point mousePoint) {
        // Convert mouse coordinates to cube coordinates
        FractionalCubeCoordinates fractionalCubeCoordinates = layout.PixelToFractionalCubeCo(layout, mousePoint);
        CubeCoordinates roundedCubeCoordinates = fractionalCubeCoordinates.round();
        System.out.println("Mouse Cube Coordinates: " + roundedCubeCoordinates);

        // Check if the mouse is inside the hexagon at roundedCubeCoordinates
        if (!roundedCubeCoordinates.equals(
                new CubeCoordinates(hoveredHexagonQ, hoveredHexagonR, -hoveredHexagonQ - hoveredHexagonR))) {
            hoveredHexagonQ = roundedCubeCoordinates.getQ();
            hoveredHexagonR = roundedCubeCoordinates.getR();
            System.out.println("Hovered Hexagon: " + hoveredHexagonQ + ", " + hoveredHexagonR);
            // Repaint the panel to update the display
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the hexagonal grid
        drawHexagonalGrid(g);

        // Draw the wider hexagon sides for the hovered hexagon
        if (hoveredHexagonQ != -800 && hoveredHexagonR != -800) {
            drawWiderHexagonSides(g,
                    new CubeCoordinates(hoveredHexagonQ, hoveredHexagonR, -hoveredHexagonQ - hoveredHexagonR), 3.0f);
        }
    }

    private void drawHexagonalGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Set color for the hexagons
        g2d.setColor(Color.BLUE);

        int gridSize = 2; // Adjust the size of the grid
        int maxDistance = 2;
        for (int q = -gridSize; q <= gridSize; q++) {
            for (int r = Math.max(-gridSize, -maxDistance); r <= Math.min(gridSize, maxDistance); r++) {
                int s = -q - r;
                if (Math.abs(s) <= gridSize && Math.abs(r) <= gridSize && Math.abs(q) <= gridSize) {
                    CubeCoordinates cubeCoordinates = new CubeCoordinates(q, r, -q - r);
                    ArrayList<Point> hexagonCorners = layout.PolygonCorners(layout, cubeCoordinates);

                    int[] xPoints = new int[hexagonCorners.size()];
                    int[] yPoints = new int[hexagonCorners.size()];

                    for (int i = 0; i < hexagonCorners.size(); i++) {
                        xPoints[i] = (int) hexagonCorners.get(i).getX();
                        yPoints[i] = (int) hexagonCorners.get(i).getY();
                    }

                    // Draw hexagon
                    g2d.drawPolygon(xPoints, yPoints, 6);

                    // Draw a red dot at the center of each hexagon
                    if (q == 0 && r == 0) {
                        Point center = layout.CubeToPixel(layout, cubeCoordinates);
                        g2d.setColor(Color.RED);
                        g2d.fillOval((int) center.getX() - 5, (int) center.getY() - 5, 10, 10);
                    }
                    g2d.setColor(Color.BLUE);
                }
            }
        }
    }

    private void drawWiderHexagonSides(Graphics g, CubeCoordinates cubeCoordinates, float strokeWidth) {
        Graphics2D g2d = (Graphics2D) g;

        // Set color for the wider hexagon sides
        g2d.setColor(Color.GREEN);
        Stroke originalStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(strokeWidth));

        int maxDistance = 2;

        if (Math.abs(cubeCoordinates.getQ()) <= maxDistance && Math.abs(cubeCoordinates.getR()) <= maxDistance
                && Math.abs(cubeCoordinates.getS()) <= maxDistance) {

            // Draw wider hexagon sides
            ArrayList<Point> hexagonCorners = layout.PolygonCorners(layout, cubeCoordinates);
            int[] xPoints = new int[hexagonCorners.size()];
            int[] yPoints = new int[hexagonCorners.size()];

            for (int i = 0; i < hexagonCorners.size(); i++) {
                Point corner = hexagonCorners.get(i);
                Point nextCorner = hexagonCorners.get((i + 1) % hexagonCorners.size());

                xPoints[i] = (int) corner.getX();
                yPoints[i] = (int) corner.getY();
                
            }

            // Draw the wider hexagon sides
            g2d.drawPolygon(xPoints, yPoints, 6);
        }
        g2d.setStroke(originalStroke);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hexagonal Grid");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);

            // Create a Layout with your desired orientation, origin, and size
            Layout layout = new Layout(Orientation.POINTY, new Point(400, 400), new Point(50, 50));

            // Create HexagonalGridPanel with the specified layout
            HexagonalGridPanel hexagonalGridPanel = new HexagonalGridPanel(layout);

            // Add HexagonalGridPanel to the frame
            frame.add(hexagonalGridPanel);

            frame.setVisible(true);
        });
    }
}
