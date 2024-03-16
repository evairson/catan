package model;

import model.buildings.Building;
import model.buildings.Colony;
import model.buildings.Road;
import model.geometry.*;
import model.geometry.Point;
import model.tiles.*;
import others.Constants;
import view.TileImageLoader;
import view.TileType;
import view.utilities.Resolution;
//import view.utilities.Resolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private HashMap<CubeCoordinates, Tile> board;
    private Layout layout;
    private int gridSize = 2;
    private ArrayList<TileVertex> verticesMap;
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
    private boolean waitingChoice = false;
    private Game game;
    private App app;

    private boolean thiefModeEnd;

    private TileVertex closestTileVertex = new TileVertex();
    private TileEdge closestTileEdge = new TileEdge();

    private Tile highlightedTile;
    private Map<String, BufferedImage> loadedImages;

    private Map<Integer, BufferedImage> diceValueImages = new HashMap<>();

    private BufferedImage boardImage = new BufferedImage(Constants.Game.WIDTH,
            Constants.Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);

    private static Font baseFont = new Font("SansSerif", Font.BOLD,
            (int) (30 / Constants.Game.DIVIDER));
    private static Font highlightedFont = new Font("SansSerif",
            Font.BOLD, (int) (35 / Constants.Game.DIVIDER));

    private Map<Building, Image> scaledBuildingImages = new HashMap<>();

    public GameBoard(Layout layout, Thief thief, Game game) {
        loadImages();
        this.thief = thief;
        this.game = game;
        board = new HashMap<CubeCoordinates, Tile>();
        this.layout = layout;
        this.initialiseBoard();
    }

    public void setThiefModeEnd(boolean b) {
        thiefModeEnd = b;
    }

    private void loadImages() {
        loadedImages = new HashMap<>();

        String basePath = "src/main/resources/building/pions/";
        String[] colors = {"Blue", "Green", "Red", "Yellow"};
        String[] buildingTypes = {"city", "colony", "road"};

        for (String color : colors) {
            for (String type : buildingTypes) {
                String path = basePath + color + "/" + type + ".png";
                try {
                    BufferedImage image = ImageIO.read(new File(path));
                    loadedImages.put(color + "_" + type, image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private BufferedImage getImageForBuilding(Building building) {
        String color = building.getOwner().getColorString();
        String type = "colony";
        if (building instanceof Road) {
            type = "road";
        } else if (building instanceof Colony) {
            Colony colony = (Colony) building;
            if (colony.getIsCity()) {
                type = "city";
            }
        }
        return loadedImages.get(color + "_" + type);
    }

    public void setApp(App app) {
        this.app = app;
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

    public TileVertex[] getNeighbourTileVerticesToVertex(TileVertex vertex) {
        TileVertex[] neighbours = new TileVertex[3];
        int index = 0;
        for (TileEdge edge : edgesMap.values()) {
            if (edge.getStart().equals(vertex.getCoordinates())) {
                for (TileVertex neighbour : verticesMap) {
                    if (neighbour.getCoordinates().equals(edge.getEnd())) {
                        if (checkIfNeighbourInArray(neighbours, neighbour)) {
                            continue;
                        }
                        neighbours[index] = neighbour;
                        index++;
                    }
                }
            }
            if (edge.getEnd().equals(vertex.getCoordinates())) {
                for (TileVertex neighbour : verticesMap) {
                    if (neighbour.getCoordinates().equals(edge.getStart())) {
                        if (checkIfNeighbourInArray(neighbours, neighbour)) {
                            continue;

                        }
                        neighbours[index] = neighbour;

                        index++;
                    }
                }
            }
        }
        for (TileVertex v : neighbours) {
            if (v != null) {
                System.out.println("Neighbour: " + v.getCoordinates());
            }
        }
        return neighbours;
    }

    public boolean checkIfNeighbourInArray(TileVertex[] neighbours, TileVertex vertex) {
        for (TileVertex v : neighbours) {
            if (v != null) {
                if (v.getCoordinates().equals(vertex.getCoordinates())) {
                    return true;
                }
            }
        }
        return false;
    }
    public TileEdge[] getNeighbourTileEdgesToVertex(TileVertex vertex) {
        TileEdge[] neighbours = new TileEdge[3];
        int i = 0;
        for (TileEdge edge : edgesMap.values()) {
            if (edge.getStart().equals(vertex.getCoordinates())
                    || edge.getEnd().equals(vertex.getCoordinates())) {
                neighbours[i] = edge;
                i++;
            }
        }
        return neighbours;
    }

    public TileEdge[] getNeighbourTileEdgesToEdge(TileEdge edge) {
        TileEdge[] neighbours = new TileEdge[4];
        int i = 0;
        for (TileEdge e : edgesMap.values()) {
            if (e.getStart().equals(edge.getStart()) || e.getEnd().equals(edge.getStart())
                    || e.getStart().equals(edge.getEnd()) || e.getEnd().equals(edge.getEnd())) {
                if (!e.equals(edge)) {
                    neighbours[i] = e;
                    i++;
                }
            }
        }
        return neighbours;
    }

    public boolean isVertexTwoRoadsAwayFromCities(TileVertex vertex) {
        TileVertex[] neighbours = getNeighbourTileVerticesToVertex(vertex);
        for (TileVertex neighbour : neighbours) {
            if (neighbour.getBuilding() != null) {
                return false;
            }
        }
        return true;
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
                }
            }
        }
        initialiseVertices();
        initialiseEdges();
        initialiseBoardImage();
        initDiceValueImages();
    }

    public void initialiseBoardImage() {
        Graphics2D g2dBoard = boardImage.createGraphics();
        double scaleFactorX = (double) Constants.Game.WIDTH / Constants.Game.BASE_WIDTH;
        double scaleFactorY = (double) Constants.Game.HEIGHT / Constants.Game.BASE_HEIGHT;

        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            Tile tile = entry.getValue();
            Polygon hexagon = new Polygon();
            ArrayList<Point> hexagonVertices = layout.polygonCorners(layout, tile.getCoordinates());

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

            Image scaledImg = img.getScaledInstance(
                    (int) (img.getWidth() * scaleFactorX / 1.5),
                    (int) (img.getHeight() * scaleFactorY / 1.5),
                    Image.SCALE_SMOOTH
            );

            int imgX = bounds.x + (bounds.width - scaledImg.getWidth(null)) / 2;
            int imgY = bounds.y + (bounds.height - scaledImg.getHeight(null)) / 2;

            g2dBoard.drawImage(scaledImg, imgX, imgY, null);
        }

        g2dBoard.dispose();
    }

    private void initDiceValueImages() {
        int[] diceValues = {2, 3, 4, 5, 6, 8, 9, 10, 11, 12}; // Les valeurs possibles des dés
        int circleDiameter = (int) (60 / Constants.Game.DIVIDER); // Diamètre de base pour les cercles

        for (int value : diceValues) {
            BufferedImage image = new BufferedImage(circleDiameter, circleDiameter,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            // Dessiner le cercle
            g2d.fillOval(0, 0, circleDiameter, circleDiameter);

            // Choix de la couleur et de la police en fonction de la valeur
            if (value == 6 || value == 8) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLACK);
            }

            g2d.setFont(baseFont);

            // Dessiner la valeur du dé
            String diceValueStr = String.valueOf(value);
            FontMetrics metrics = g2d.getFontMetrics(baseFont);
            int xText = (circleDiameter - metrics.stringWidth(diceValueStr)) / 2;
            int yText = ((circleDiameter - metrics.getHeight()) / 2) + metrics.getAscent();
            g2d.drawString(diceValueStr, xText, yText);

            g2d.dispose(); // Libérer les ressources du Graphics2D
            diceValueImages.put(value, image);
        }
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
        verticesMap = new ArrayList<>();

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

                for (TileVertex v : verticesMap) {
                    Point storedVertex = v.getCoordinates();
                    v.addTile(tile);
                    if (arePointsEqual(vertex, storedVertex)) {
                        // Si oui, ajouter la tuile actuelle à la liste des tuiles
                        // associées à ce sommet
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
                    tileVertex.setCoordinates(vertex);
                    tileVertex.setCoordinates(vertex);
                    // Arrondir les coordonnées pour éviter les erreurs d'arrondi
                    verticesMap.add(tileVertex);
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

    private boolean buildingIsCity(Building building) {
        if (building instanceof Colony) {
            return ((Colony) building).getIsCity();
        }
        return false;
    }
    private void drawBuildingImage(Graphics2D g2d, Building building, Point vertex) {
        boolean isCity = buildingIsCity(building);
        if (scaledBuildingImages.get(building) == null) {
            BufferedImage img = getImageForBuilding(building);
            int size = (isCity ? 90 : 60);
            scaledBuildingImages.put(building, img.getScaledInstance((int) (size / Resolution.divider()),
                (int) (size / Resolution.divider()), Image.SCALE_SMOOTH));
        }
        int placement = (isCity ? 40 : 30);

        g2d.drawImage(scaledBuildingImages.get(building),
                (int) vertex.getX() - (int) (placement / Resolution.divider()),
                (int) vertex.getY() - (int) (placement / Resolution.divider()), null);
    }

    private void drawVertices(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK); // Color for the vertices
        // Draw the vertices

        for (TileVertex v : verticesMap) {
            Building building = v.getBuilding();
            if (building != null) {
                drawBuildingImage(g2d, building, v.getCoordinates());
            }
        }
    }

    public void drawImagesInHexes(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        if (boardImage != null) {
            if (thiefMode || thiefModeEnd) {
                initialiseBoardImage();
                if (thiefModeEnd) {
                    thiefModeEnd = false;
                }
            }
            g2d.drawImage(boardImage, 0, 0, null);
        }

    }

    private void drawEdgeWithImage(Graphics2D g2d, Point start, Point end, BufferedImage edgeImage) {
        // Sauvegarder l'état actuel du contexte Graphics2D
        AffineTransform oldTransform = g2d.getTransform();

        // Calculer le milieu de l'arête et l'angle de rotation
        int midX = (int) ((start.getX() + end.getX()) / 2);
        int midY = (int) ((start.getY() + end.getY()) / 2);
        double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()) + Math.PI / 2;

        // Mise à l'échelle de l'image
        int scaledWidth = (int) (edgeImage.getWidth(null) * 0.38 / Constants.Game.DIVIDER);
        int scaledHeight = (int) (edgeImage.getHeight(null) * 0.38 / Constants.Game.DIVIDER);
        Image scaledImage = edgeImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // Appliquer la transformation
        AffineTransform transform = new AffineTransform(oldTransform);
        transform.translate(midX, midY);
        transform.rotate(angle);
        g2d.setTransform(transform);

        // Dessiner l'image
        g2d.drawImage(scaledImage, -scaledWidth / 2, -scaledHeight / 2, null);

        // Rétablir l'état original du contexte Graphics2D
        g2d.setTransform(oldTransform);
    }


    private void drawEdges(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        for (TileEdge edge : edgesMap.values()) {
            if (edge.getBuilding() != null) {
                BufferedImage edgeImage = getImageForBuilding(edge.getBuilding());
                drawEdgeWithImage(g2d, edge.getStart(), edge.getEnd(), edgeImage);
            }
        }
    }

    public void drawBoard(Graphics g) {
        drawImagesInHexes(g);
        drawEdges(g);
        drawVertices(g);

        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            CubeCoordinates cubeCoord = entry.getKey();
            if (!thief.getTile().equals(entry.getValue())) {
                Point pixel = layout.cubeToPixel(layout, cubeCoord);
                int diceValue = entry.getValue().getDiceValue();

                BufferedImage image = diceValueImages.get(diceValue);
                if (image != null) {
                    int x = (int) pixel.getX() - (image.getWidth() / 2);
                    int y = (int) pixel.getY() - (image.getHeight() / 2);
                    g.drawImage(image, x, y, null);
                }
            }
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
        g2d.setColor(Color.black);
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
        app.getGamePanel().repaint();
    }

    public void changeThief() {
        thief.setTile(highlightedTile);
        game.setThiefMode(false);
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
        closestTileVertex = new TileVertex();
        for (TileVertex v : verticesMap) {
            Point vertex = v.getCoordinates();
            double distance = vertex.distance(mousePosition);
            if  (closestTileVertex == null || distance < minDistanceToVertex) {
                closestTileVertex = v;
                minDistanceToVertex = distance;
                closestVertex = v.getCoordinates();
            }
        }
        return closestTileVertex;
    }

}
