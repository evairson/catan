package model;

import model.geometry.*;
import model.geometry.Point;
import model.tiles.*;
import others.Constants;
import view.TileImageLoader;
import view.TileType;
import model.geometry.CubeCoordinates;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import view.TileType.*;

public class GameBoard {
    private HashMap<CubeCoordinates, Tile> board;
    private Layout layout;
    private int gridSize = 2;
    private HashMap<Point, TileVertex> verticesMap;
    private HashMap<Point, TileEdge> edgesMap;
    // sert à stocker les coordonnées du sommet le plus proche de la souris
    // peut aller dans une autre classe...
    private Point closestVertex = new Point(0, 0);
    private Point closestEdge = new Point(0, 0);
    private double minDistanceToEdge;
    private Point mousePosition;
    private double minDistanceToVertex;

    private boolean lookingForVertex = false;
    private boolean lookingForEdge = false;
    private TileVertex closestTileVertex = new TileVertex();
    private TileEdge closestTileEdge = new TileEdge();

    public GameBoard(Layout layout) {
        board = new HashMap<CubeCoordinates, Tile>();
        this.layout = layout;
        this.initialiseBoard();
        // rendre la centre et la taille de la grille dynamique

    }

    public TileVertex getClosestTileVertex() {
        return closestTileVertex;
    }

    public TileEdge getClosestTileEdge() {
        return closestTileEdge;
    }

    public void setClosestTileVertex(TileVertex closestTileVertex) {
        this.closestTileVertex = closestTileVertex;
    }

    public void setClosestTileEdge(TileEdge closestTileEdge) {
        this.closestTileEdge = closestTileEdge;
    }

    public void setLookingForVertex(boolean lookingForVertex) {
        this.lookingForVertex = lookingForVertex;
    }

    public void setLookingForEdge(boolean lookingForEdge) {
        this.lookingForEdge = lookingForEdge;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public boolean isLookingForVertex() {
        return lookingForVertex;
    }

    public boolean isLookingForEdge() {
        return lookingForEdge;
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

    public void addTile(int q, int r, int diceValue) {
        int s = -q - r;
        board.put(new CubeCoordinates(q, r, s), new Tile(q, r, diceValue));
    }

    public void addTile(int q, int r, int diceValue, TileType resourceType) {
        int s = -q - r;
        board.put(new CubeCoordinates(q, r, s), new Tile(q, r, diceValue, resourceType));
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
        int presetTileDiceValue = 0;
        int presetTileResourceType = 0;
        for (int q = -gridSize; q <= gridSize; q++) {
            for (int r = -gridSize; r <= gridSize; r++) {
                int s = -q - r;
                if (Math.abs(s) <= gridSize && Math.abs(r) <= gridSize && Math.abs(q) <= gridSize) {
                    int tileDiceValue = Constants.BoardConstants.TILE_DICE_VALUES[presetTileDiceValue];
                    int tileResourceType = 0;
                    if (presetTileResourceType < Constants.BoardConstants.TILE_TYPES.length) {
                        tileResourceType = Constants.BoardConstants.TILE_TYPES[presetTileResourceType];
                    }
                    if (tileDiceValue != 0) {
                        presetTileResourceType++;
                    } else {
                        tileResourceType = 0;
                    }
                    addTile(q, r, tileDiceValue, getTileType(tileResourceType));
                    presetTileDiceValue++;

                    System.out.println("Tile (" + q + ", " + r + ", " + s + "), Ressource Type: "
                            + tileResourceType + " added to the board");
                }
            }
        }
        initialiseVertices();
        initialiseEdges();
    }

    private TileType getTileType(int resourceType) {
        switch (resourceType) {
            case 1:
                return TileType.WOOD;
            case 2:
                return TileType.WHEAT;
            case 3:
                return TileType.CLAY;
            case 4:
                return TileType.WOOL;
            case 5:
                return TileType.ORE;
            default:
                return TileType.DESERT;
        }
    }

    private void initialiseVertices() {
        verticesMap = new HashMap<>();

        // Parcourir toutes les tuiles du plateau
        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            CubeCoordinates cubeCoord = entry.getKey();
            Tile tile = entry.getValue();

            // Obtenir les coordonnées des sommets de la tuile
            ArrayList<Point> hexagonVertices = layout.polygonCorners(layout, cubeCoord);
            // Pour chaque sommet de la tuile
            for (Point vertex : hexagonVertices) {
                // Vérifier si ce sommet est déjà dans la map
                boolean found = false;
                for (Point storedVertex : verticesMap.keySet()) {
                    if (arePointsEqual(vertex, storedVertex)) {
                        // Si oui, ajouter la tuile actuelle à la liste des tuiles
                        // associées à ce sommet
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
            ArrayList<Point> hexagonVertices = layout.polygonCorners(layout, cubeCoord);
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
        return Math.abs(p1.getX() - p2.getX()) < epsilon
                && Math.abs(p1.getY() - p2.getY()) < epsilon;
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        return outputImage;
    }

    private void drawVertices(Graphics g) {


        Graphics2D g2d = (Graphics2D) g;

        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            Tile tile = entry.getValue();
            TileType resource = tile.getResourceType();
            ArrayList<Point> hexagonVertices = layout.polygonCorners(layout, tile.getCoordinates());

            Map<TileType, BufferedImage> tileImages = TileImageLoader.loadTileImages();
            BufferedImage img = tileImages.get(tile.getResourceType());
            img = resizeImage(img, img.getWidth() / 2, img.getHeight() / 2);

            double centerX = 0;
            double centerY = 0;
            for (Point vertex : hexagonVertices) {
                centerX += vertex.getX();
                centerY += vertex.getY();
            }

            centerX /= hexagonVertices.size();
            centerY /= hexagonVertices.size();

            Rectangle2D.Float rect = new Rectangle2D.Float(
                    (float) centerX - img.getWidth() / 2f,
                    (float) centerY - img.getHeight() / 2f,
                    img.getWidth(),
                    img.getHeight());

            TexturePaint tp = new TexturePaint(img, rect);
            g2d.setPaint(tp);
            Polygon hexagon = new Polygon();
            for (Point vertex : hexagonVertices) {
                hexagon.addPoint((int) vertex.getX(), (int) vertex.getY());
            }
            g2d.fillPolygon(hexagon);
        }
        // Draw the vertices
        g2d.setColor(Color.BLACK); // Color for the vertices
        for (Point vertex : verticesMap.keySet()) {
            g2d.fillOval((int) vertex.getX() - 2, (int) vertex.getY() - 2, 4, 4);
        }
    }

    private void drawEdges(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK); // Couleur des arêtes
        for (TileEdge edge : edgesMap.values()) {
            if (edge.getBuilding() != null) {
                g2d.setColor(edge.getBuilding().getColorInAwt());
                g2d.setStroke(new BasicStroke(6));
            } else {
                g2d.setColor(Color.black);
                g2d.setStroke(new BasicStroke(2));
            }
            g2d.drawLine((int) edge.getStart().getX(), (int) edge.getStart().getY(),
                    (int) edge.getEnd().getX(),
                    (int) edge.getEnd().getY()); // Dessiner l'arête
        }
    }

    public void drawBoard(Graphics g) {

        drawVertices(g);
        drawEdges(g);
        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            CubeCoordinates cubeCoord = entry.getKey();
            drawText(g, entry.getValue().getDiceValue() + "", layout.cubeToPixel(layout, cubeCoord));
        }
    }

    public void draw(Graphics g) {
        drawBoard(g);
        if (minDistanceToVertex < 20) {
            g.setColor(Color.RED);
            g.fillOval((int) closestVertex.getX() - 10,
                    (int) closestVertex.getY() - 10, 20, 20);
        }
        try {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            TileEdge edge = this.edgesMap.get(this.closestEdge);
            g2d.setStroke(new BasicStroke(6));
            g2d.drawLine((int) edge.getStart().getX(), (int) edge.getStart().getY(),
                    (int) edge.getEnd().getX(),
                    (int) edge.getEnd().getY());
        } catch (Exception e) { // null seulement la première fois qu'on survole un hexagone
        }
    }

    public void mouseMoved(MouseEvent e) {
        mousePosition = new Point(e.getX(), e.getY());
        minDistanceToVertex = Double.MAX_VALUE;
        minDistanceToEdge = Double.MAX_VALUE;
        closestVertex = null;
        closestEdge = null;

        if (lookingForVertex) {
            closestTileVertex = findClosestVertex();
        } else if (lookingForEdge) {
            closestTileEdge = findClosestEdge();
        }

    }

    public TileEdge findClosestEdge() {
        TileEdge closestTileEdge = new TileEdge();
        for (Point edge : this.edgesMap.keySet()) {
            double distance = edge.distance(mousePosition);
            if (distance < minDistanceToEdge) {
                minDistanceToEdge = distance;
                closestEdge = edge;
                this.closestEdge = closestEdge;
                closestTileEdge = this.edgesMap.get(edge);
            }
        }
        return closestTileEdge;
    }

    public TileVertex findClosestVertex() {
        TileVertex closestTileVertex = new TileVertex();
        for (Point vertex : this.verticesMap.keySet()) {
            double distance = vertex.distance(mousePosition);
            if (distance < minDistanceToVertex) {
                minDistanceToVertex = distance;
                closestVertex = vertex;
                this.closestVertex = closestVertex;
                closestTileVertex = this.verticesMap.get(vertex);
            }
        }
        return this.verticesMap.get(this.closestVertex);
    }

    private void drawText(Graphics g, String text, Point center) {
        if (text.equals("0")) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE); // Set the color of the text

        // Set the font and size of the text
        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(font);

        // Draw the text
        FontMetrics fontMetrics = g2d.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();

        int startX = (int) center.getX() - textWidth / 2;
        int startY = (int) center.getY() + textHeight / 2;

        g2d.drawString(text, startX, startY);
    }
}
