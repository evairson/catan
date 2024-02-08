import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameBoard {
    private HashMap<CubeCoordinates, Tile> board;
    private Layout layout;
    private int gridSize = 2;
    private HashMap<Point, TileVertex> verticesMap;
    private HashMap<Point, TileEdge> edgesMap;

    //sert à stocker les coordonnées du sommet le plus proche de la souris
    //peut aller dans une autre classe... git
    public Point closestVertex = new Point(0, 0);
    public Point closestEdge = new Point(0, 0);

    public GameBoard(Layout layout) {
        board = new HashMap<CubeCoordinates, Tile>();
        this.layout = layout;
        // rendre la centre et la taille de la grille dynamique

    }

    public void drawThickVertex(Point vertex) {

    }

    public void addTile(Tile t) {
        int q = t.getQ();
        int r = t.getR();
        int s = -q - r;
        board.put(new CubeCoordinates(q, r, s), t);
    }

    public void addTile(int q, int r) {
        int s = -q - r;
        board.put(new CubeCoordinates(q, r, s), new Tile(q, r));
    }

    public Tile getTile(int q, int r) {
        int s = -q - r;
        return board.get(new CubeCoordinates(q, r, s));
    }

    public HashMap<CubeCoordinates, Tile> getBoard() {
        return board;
    }

    public void initialiseBoard() {
        int maxDistance = this.gridSize;
        for (int q = -gridSize; q <= gridSize; q++) {
            for (int r = -gridSize; r <= gridSize; r++) {
                int s = -q - r;
                if (Math.abs(s) <= gridSize && Math.abs(r) <= gridSize && Math.abs(q) <= gridSize) {
                    addTile(q, r);
                }
            }
        }
        initialiseVertices();
        initialiseEdges();
    }

    private void initialiseVertices() {
        verticesMap = new HashMap<>();

        // Parcourir toutes les tuiles du plateau
        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            CubeCoordinates cubeCoord = entry.getKey();
            Tile tile = entry.getValue();

            // Obtenir les coordonnées des sommets de la tuile
            ArrayList<Point> hexagonVertices = layout.PolygonCorners(layout, cubeCoord);
            // Pour chaque sommet de la tuile
            for (Point vertex : hexagonVertices) {
                // Vérifier si ce sommet est déjà dans la map
                boolean found = false;
                for (Point storedVertex : verticesMap.keySet()) {
                    if (arePointsEqual(vertex, storedVertex)) {
                        // Si oui, ajouter la tuile actuelle à la liste des tuiles associées à ce sommet
                        verticesMap.get(storedVertex).addTile(tile);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Si le sommet n'est pas trouvé, créer un nouvel objet TileVertex pour ce
                    // sommet
                    TileVertex tileVertex = new TileVertex();
                    // Ajouter la tuile actuelle à la liste des tuiles associées à ce sommet
                    tileVertex.addTile(tile);
                    // Mettre ce sommet dans la map avec comme valeur l'objet TileVertex
                    vertex = new Point((Math.round(vertex.getX())), (Math.round(vertex.getY())));
                    // Arrondir les coordonnées pour éviter les erreurs d'arrondi
                    verticesMap.put(vertex, tileVertex);
                }
            }
        }
    }

    private void initialiseEdges() {
        edgesMap = new HashMap<>();
        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            CubeCoordinates cubeCoord = entry.getKey();
            Tile tile = entry.getValue();
            ArrayList<Point> hexagonVertices = layout.PolygonCorners(layout, cubeCoord);
            for (int i = 0; i < 6; i++) {
                Point start = hexagonVertices.get(i);
                Point end = hexagonVertices.get((i + 1) % 6);
                start = new Point((Math.round(start.getX())), (Math.round(start.getY())));
                end = new Point((Math.round(end.getX())), (Math.round(end.getY())));
                TileEdge edge = new TileEdge(start, end);
                Point edgeMidPoint = new Point((int) ((start.getX() + end.getX()) / 2),
                        (int) ((start.getY() + end.getY()) / 2));
                edgesMap.put(edgeMidPoint, edge);
            }
        }
    }

    private boolean arePointsEqual(Point p1, Point p2) {
        // Définir la marge d'erreur acceptable
        double epsilon = 1.0;

        // Vérifier si les coordonnées x et y des points sont proches les unes des
        // autres avec une marge d'erreur
        return Math.abs(p1.getX() - p2.getX()) < epsilon && Math.abs(p1.getY() - p2.getY()) < epsilon;
    }

    private void drawVertices(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK); // Couleur des sommets

        for (Point vertex : verticesMap.keySet()) {
            g2d.fillOval((int) vertex.getX() - 2, (int) vertex.getY() - 2, 4, 4); // Dessiner le sommet
        }
    }

    private void drawEdges(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK); // Couleur des arêtes
        for (TileEdge edge : edgesMap.values()) {
            g2d.drawLine((int) edge.getStart().getX(), (int) edge.getStart().getY(), (int) edge.getEnd().getX(),
                    (int) edge.getEnd().getY()); // Dessiner l'arête
        }
    }

    public void drawBoard(Graphics g) {
        drawVertices(g);
        drawEdges(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer un Layout avec votre orientation, origine et taille préférées
            Layout layout = new Layout(Orientation.POINTY, new Point(400, 400), new Point(50, 50));

            // Créer un GameBoard avec le Layout
            GameBoard gameBoard = new GameBoard(layout);

            // Initialiser le tableau de jeu
            gameBoard.initialiseBoard();

            JFrame frame = new JFrame("Test Game Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);
            // Créer un JPanel personnalisé pour dessiner le jeu
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    gameBoard.drawBoard(g);
                    try {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setColor(Color.RED);
                        TileEdge edge = gameBoard.edgesMap.get(gameBoard.closestEdge);
                        g2d.setStroke(new BasicStroke(6));
                        g2d.drawLine((int) edge.getStart().getX(), (int) edge.getStart().getY(),
                                (int) edge.getEnd().getX(),
                                (int) edge.getEnd().getY());
                    } catch (Exception e) {
                    }

                }
            };

            panel.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    Point mousePosition = new Point(e.getX(), e.getY());
                    double minDistanceToVertex = Double.MAX_VALUE;
                    double minDistanceToEdge = Double.MAX_VALUE;
                    Point closestVertex = null;
                    Point closestEdge = null;
                    for (Point vertex : gameBoard.verticesMap.keySet()) {
                        double distance = vertex.distance(mousePosition);
                        if (distance < minDistanceToVertex) {
                            minDistanceToVertex = distance;
                            closestVertex = vertex;
                            gameBoard.closestVertex = closestVertex;
                        }
                    }
                    for (Point edge : gameBoard.edgesMap.keySet()) {
                        double distance = edge.distance(mousePosition);
                        if (distance < minDistanceToEdge) {
                            minDistanceToEdge = distance;
                            closestEdge = edge;
                            gameBoard.closestEdge = closestEdge;
                        }
                    }

                    if (minDistanceToVertex < 20) {
                        Graphics2D g2d = (Graphics2D) panel.getGraphics();
                        g2d.setColor(Color.RED);
                        g2d.fillOval((int) closestVertex.getX() - 10, (int) closestVertex.getY() - 10, 20, 20);
                    } else {
                        // Clear previous red oval
                        panel.repaint();
                    }

                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }

}