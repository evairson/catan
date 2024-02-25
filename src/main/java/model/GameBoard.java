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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private double minDistanceToCenterTile;
    private Thief thief;
    private boolean thiefMode;
    private Map<TileType, BufferedImage> tileImages = TileImageLoader.loadAndResizeTileImages(false);
    private Map<TileType, BufferedImage> tileImagesS = TileImageLoader.loadAndResizeTileImages(true);
    private boolean lookingForVertex = false;
    private boolean lookingForEdge = false;
    private boolean placingRoad = false;
    private boolean placingColony = false;
    private boolean placingCity = false;

    private TileVertex closestTileVertex = new TileVertex();
    private TileEdge closestTileEdge = new TileEdge();

    private Tile highlightedTile;

    public GameBoard(Layout layout, Thief thief) {
        this.thief = thief;
        board = new HashMap<CubeCoordinates, Tile>();
        this.layout = layout;
        this.initialiseBoard();
        // rendre la centre et la taille de la grille dynamique

    }

    public void setThiefMode(boolean b) {
        thiefMode = b;
    }

    public boolean getThiefMode() {
        return thiefMode;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setPlacingRoad(boolean placingRoad) {
        this.placingRoad = placingRoad;
    }
    public void setPlacingColony(boolean placingColony) {
        this.placingColony = placingColony;
    }
    public void setPlacingCity(boolean placingCity) {
        this.placingCity = placingCity;
    }
    public boolean isPlacingRoad() {
        return placingRoad;
    }
    public boolean isPlacingColony() {
        return placingColony;
    }
    public boolean isPlacingCity() {
        return placingCity;
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
        Tile tile = new Tile(q, r, diceValue, resourceType);
        if (resourceType == TileType.DESERT) {
            thief.setTile(tile);
        }
        board.put(new CubeCoordinates(q, r, s), tile);
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
        g2d.setColor(Color.BLACK); // Color for the vertices
        // Draw the vertices
        for (Point vertex : verticesMap.keySet()) {
            if (verticesMap.get(vertex).getBuilding() != null) {
                g2d.setColor(verticesMap.get(vertex).getBuilding().getColorInAwt());
                g2d.fillOval((int) vertex.getX() - 4, (int) vertex.getY() - 4, 8, 8);
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillOval((int) vertex.getX() - 2, (int) vertex.getY() - 2, 4, 4);
            }
        }
    }

    public void drawImagesInHexes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            Tile tile = entry.getValue();
            Polygon hexagon = new Polygon();
            ArrayList<Point> hexagonVertices = layout.polygonCorners(layout, tile.getCoordinates());
            boolean highlight = false;

            for (Point vertex : hexagonVertices) {
                hexagon.addPoint((int) vertex.getX(), (int) vertex.getY());
            }
            Rectangle bounds = hexagon.getBounds();
            BufferedImage img;
            if (thiefMode && highlightedTile == tile) {
                img = tileImagesS.get(tile.getResourceType());
            } else {
                img = tileImages.get(tile.getResourceType());
            }

            int imgX = bounds.x + (bounds.width - img.getWidth()) / 2;
            int imgY = bounds.y + (bounds.height - img.getHeight()) / 2;

            g2d.drawImage(img, imgX, imgY, null);
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
        drawImagesInHexes(g);
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
        drawThief(g);
    }

    public void drawThief(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Point p = layout.cubeToPixel(layout, thief.getTile().getCoordinates());
        g2d.setColor(Color.white);
        g2d.fillOval((int) p.getX() - 10, (int) p.getY() - 10, 20, 20);
    }

    public void mouseMoved(MouseEvent e) {
        mousePosition = new Point(e.getX(), e.getY());
        minDistanceToVertex = Double.MAX_VALUE;
        minDistanceToEdge = Double.MAX_VALUE;
        minDistanceToCenterTile = Double.MAX_VALUE;
        closestVertex = null;
        closestEdge = null;

        if (lookingForVertex) {
            closestTileVertex = findClosestVertex();
        } else if (lookingForEdge) {
            closestTileEdge = findClosestEdge();
        } else if (thiefMode) {
            for (CubeCoordinates coordinatesTile : this.board.keySet()) {
                double distance = layout.cubeToPixel(layout, coordinatesTile).distance(mousePosition);
                if (distance < minDistanceToCenterTile) {
                    minDistanceToCenterTile = distance;
                    highlightedTile = board.get(coordinatesTile);
                }
            }
        }

    }

    public void changeThief(MouseEvent e) {
        thief.setTile(highlightedTile);
        thiefMode = false;
    }

    public TileEdge findClosestEdge() {
        TileEdge closestTileEdge = new TileEdge();
        for (Point edge : this.edgesMap.keySet()) {
            double distance = edge.distance(mousePosition);
            if (distance < minDistanceToEdge) {
                minDistanceToEdge = distance;
                closestEdge = edge;
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
