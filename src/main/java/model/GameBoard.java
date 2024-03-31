package model;

import model.buildings.*;
import model.geometry.*;
import model.tiles.*;
import network.NetworkObject;
import network.NetworkObject.TypeObject;
import network.PlayerClient;
import others.Constants;
import start.Main;
import view.TileImageLoader;
import view.TileType;
import view.utilities.Resolution;
//import view.utilities.Resolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements Serializable {
    private LinkedHashMap<CubeCoordinates, Tile> board;
    private final Map<TileVertex, Harbor> harborMap = new HashMap<>();
    private Layout layout;
    private int gridSize = 2;
    private ArrayList<TileVertex> vertices;
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
    private Map<TileType, BufferedImage> tileImages;
    private Map<TileType, BufferedImage> tileImagesS;
    private boolean lookingForVertex = false;
    private boolean lookingForEdge = false;
    private boolean placingRoad = false;
    private boolean placingColony = false;
    private boolean placingCity = false;
    private Game game;
    private App app;

    private boolean thiefModeEnd;

    private TileVertex closestTileVertex = new TileVertex();
    private TileEdge closestTileEdge = new TileEdge();

    private Tile highlightedTile;
    private Map<String, BufferedImage> loadedImages;

    private Map<Integer, BufferedImage> diceValueImages = new HashMap<>();

    private BufferedImage boardImage;

    private static Font baseFont = new Font("SansSerif", Font.BOLD,
            (int) (30 / Constants.Game.DIVIDER));
    private static Font highlightedFont = new Font("SansSerif",
            Font.BOLD, (int) (35 / Constants.Game.DIVIDER));

    private Map<Building, Image> scaledBuildingImages = new HashMap<>();
    private BufferedImage harborImage;

    public GameBoard(Thief thief, Game game) {

        this.thief = thief;

        board = new LinkedHashMap<CubeCoordinates, Tile>();
        this.game = game;
        this.initialiseBoard();
    }

    private void loadHarborImage() {
        try {
            BufferedImage originalImage = ImageIO.read(new File("src/main/resources/harbor.png"));
            // Ajustez la taille comme nécessaire
            int width = originalImage.getWidth() / 5; // Exemple de réduction de la taille
            int height = originalImage.getHeight() / 5;
            harborImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = harborImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawPorts(Graphics g) {
        for (Map.Entry<TileVertex, Harbor> entry : harborMap.entrySet()) {
            TileVertex vertex = entry.getKey();
            Point location = vertex.getCoordinates();
            // Dessinez l'image du port à l'emplacement du vertex
            g.drawImage(harborImage, (int) location.getX() - harborImage.getWidth() / 2,
                    (int) location.getY() - harborImage.getHeight() / 2, null);
        }
    }

    public void initialisePorts() {
        // Définir les coordonnées des ports classiques
        Point[] classicPortsPoints = {
            new Point(218, 155),
            new Point(279, 120),
            new Point(582, 225),
            new Point(642, 260),
            new Point(703, 365),
            new Point(703, 435),
            new Point(279, 680),
            new Point(218, 645)
        };

        // Définir les coordonnées des ports spécialisés et leur type de ressource associé
        Map<Point, TileType> specializedPortsPoints = Map.of(
            new Point(400, 120), TileType.WOOL,
            new Point(461, 155), TileType.WOOL,
            new Point(642, 540), TileType.CLAY,
            new Point(582, 575), TileType.CLAY,
            new Point(461, 645), TileType.WOOD,
            new Point(400, 680), TileType.WOOD,
            new Point(158, 540), TileType.WHEAT,
            new Point(158, 470), TileType.WHEAT,
            new Point(158, 330), TileType.ORE,
            new Point(158, 260), TileType.ORE
        );

        // Initialiser les ports classiques
        for (Point point : classicPortsPoints) {
            TileVertex harborVertex = findTileVertexByPoint(point);
            if (harborVertex != null) {
                Harbor port = new Harbor(harborVertex);
                harborVertex.setHarbor(port);
                harborMap.put(harborVertex, port);
            }
        }

        // Initialiser les ports spécialisés
        specializedPortsPoints.forEach((point, type) -> {
            TileVertex harborVertex = findTileVertexByPoint(point);
            if (harborVertex != null) {
                SpecializedHarbor specializedPort = new SpecializedHarbor(harborVertex, type);
                harborVertex.setHarbor(specializedPort);
                harborMap.put(harborVertex, specializedPort);
            }
        });
    }

    private TileVertex findTileVertexByPoint(Point point) {
        for (TileVertex vertex : vertices) {
            if (vertex.getCoordinates().equals(point)) {
                return vertex;
            }
        }
        return null;
    }

    public void setThiefModeEnd(boolean b) {
        thiefModeEnd = b;
    }

    private void loadImages() {
        boardImage = new BufferedImage(Constants.Game.WIDTH,
        Constants.Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
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

    public HashMap<Point, TileEdge> getEdgeMap() {
        return edgesMap;
    }

    public ArrayList<TileVertex> getVertices() {
        return vertices;
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
                for (TileVertex neighbour : vertices) {
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
                for (TileVertex neighbour : vertices) {
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

    public boolean checkIfNeighbourInArray(TileEdge[] neighbours, TileEdge edge) {
        for (TileEdge e : neighbours) {
            if (e != null) {
                if (e.getStart().equals(edge.getStart()) && e.getEnd().equals(edge.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }

    public TileVertex[] getNeighbourTileVerticesToEdge(TileEdge edge) {
        TileVertex[] neighbours = new TileVertex[2];
        int i = 0;
        for (TileVertex vertex : vertices) {
            if (vertex.getCoordinates().equals(edge.getStart())) {
                neighbours[i] = vertex;
                i++;
            }
            if (vertex.getCoordinates().equals(edge.getEnd())) {
                neighbours[i] = vertex;
                i++;
            }
        }
        return neighbours;
    }

    public TileEdge[] getNeighbourTileEdgesToEdge(TileEdge edge) {
        TileEdge[] neighbours = new TileEdge[4];
        int i = 0;
        for (TileEdge e : edgesMap.values()) {
            if (checkIfNeighbourInArray(neighbours, e)) {
                continue;
            }
            if ((e.getStart().equals(edge.getStart()) && !e.getEnd().equals(edge.getEnd()))
                || (e.getEnd().equals(edge.getStart()) && !e.getStart().equals(edge.getEnd()))) {
                neighbours[i] = e;
                i++;
            }
            if ((e.getStart().equals(edge.getEnd()) && !e.getEnd().equals(edge.getStart()))
                || (e.getEnd().equals(edge.getEnd()) && !e.getStart().equals(edge.getStart()))) {
                neighbours[i] = e;
                i++;
            }
        }
        return neighbours;

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

    public boolean isVertexTwoRoadsAwayFromCities(TileVertex vertex) {
        TileVertex[] neighbours = getNeighbourTileVerticesToVertex(vertex);
        for (TileVertex v : neighbours) {
            if (v != null) {
                if (v.getBuilding() != null) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isRoadNextToCity(TileEdge edge, Player player) {
        TileVertex[] neighbours = getNeighbourTileVerticesToEdge(edge);
        for (TileVertex v : neighbours) {
            if (v != null) {
                if (v.getBuilding() != null) {
                    if (v.getBuilding().getOwner() == player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isRoadNextToRoad(TileEdge edge, Player player) {
        TileEdge[] neighbours = getNeighbourTileEdgesToEdge(edge);
        for (TileEdge e : neighbours) {
            if (e != null) {
                if (e.getBuilding() != null) {
                    if (e.getBuilding().getOwner() == player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isVertexNextToRoad(TileVertex vertex, Player player) {
        TileEdge[] neighbours = getNeighbourTileEdgesToVertex(vertex);
        for (TileEdge e : neighbours) {
            if (e != null) {
                if (e.getBuilding() != null) {
                    if (e.getBuilding().getOwner() == player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canPlaceColony(TileVertex vertex, Player player) {
        if (game.getCurrentPlayer().getFreeColony()) {
            return true;
        }
        if (vertex.getBuilding() != null) {
            return false;
        }
        if (!isVertexTwoRoadsAwayFromCities(vertex)) {
            return false;
        }
        if (!isVertexNextToRoad(vertex, player)) {
            return false;
        }
        return true;
    }

    public boolean canPlaceRoad(TileEdge edge, Player player) {
        if (edge.getBuilding() != null) {
            return false;
        }
        if (isRoadNextToCity(edge, player)) {
            return true;
        }
        if (isRoadNextToRoad(edge, player)) {
            return true;
        }
        return false;
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

    public LinkedHashMap<CubeCoordinates, Tile> getBoard() {
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
        vertices = new ArrayList<>();

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

                for (TileVertex v : vertices) {
                    Point storedVertex = v.getCoordinates();
                    if (arePointsEqual(vertex, storedVertex)) {
                        v.addTile(tile);
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
                    tileVertex.setId(TileVertex.getIdClass());
                    TileVertex.addIdClass();
                    // Ajouter la tuile actuelle à la liste des tuiles associées à ce sommet
                    tileVertex.addTile(tile);
                    // Mettre ce sommet dans la map avec comme valeur l'objet TileVertex
                    vertex = new Point((Math.round(vertex.getX())), (Math.round(vertex.getY())));
                    tileVertex.setCoordinates(vertex);
                    // Arrondir les coordonnées pour éviter les erreurs d'arrondi
                    vertices.add(tileVertex);
                }
            }
        }
    }

    private void initialiseEdges() {
        TileEdge.resetIdClass();
        edgesMap = new LinkedHashMap<>();
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
                edge.setId(TileEdge.getIdClass());
                TileEdge.addIdClass();
                Point edgeMidPoint = new Point((int) ((start.getX() + end.getX()) / 2),
                        (int) ((start.getY() + end.getY()) / 2));
                if (edgesMapContainsEdge(edge)) {
                    continue;
                }
                edgesMap.put(edgeMidPoint, edge);
            }
        }
    }

    public boolean edgesMapContainsEdge(TileEdge edge) {
        for (TileEdge e : edgesMap.values()) {
            if (e.getStart().equals(edge.getStart()) && e.getEnd().equals(edge.getEnd())) {
                return true;
            } else if (e.getStart().equals(edge.getEnd()) && e.getEnd().equals(edge.getStart())) {
                return true;
            }
        }
        return false;
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

        for (TileVertex v : vertices) {
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
        drawPorts(g);

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
        if (minDistanceToVertex < 20 && lookingForVertex) {
            g.setColor(Color.RED);
            g.fillOval((int) closestVertex.getX() - 10,
                    (int) closestVertex.getY() - 10, 20, 20);
        }
        if (lookingForEdge) {
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
                    App.getGamePanel().repaint();
                }
            }
        }
    }

    public void changeThiefNetwork() {
        if (Main.hasServer()) {
            try {
                PlayerClient player = ((PlayerClient) game.getPlayerClient());
                NetworkObject gameObject;
                gameObject = new NetworkObject(TypeObject.Game, "changeThief", player.getId(),
                    highlightedTile.getId());
                player.getOut().writeUnshared(gameObject);
                player.getOut().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            changeThief();
        }
    }

    public void changeThiefBot() {
        int nbAlea = (int) (Math.random() * board.size());
        for (Tile tile : board.values()) {
            if (nbAlea == tile.getId()) {
                highlightedTile = tile;
            }
        }
        changeThief();
    }

    public void changeThief() {
        thief.setTile(highlightedTile);
        game.setThiefMode(false);
        thiefMode = false;
        App.getGamePanel().repaint();
        App.getActionPlayerPanel().update();
    }

    public TileEdge findClosestEdge() {
        for (Point edge : this.edgesMap.keySet()) {
            double distance = edge.distance(mousePosition);
            if (distance < minDistanceToEdge) {
                minDistanceToEdge = distance;
                closestEdge = edge;
                closestTileEdge = this.edgesMap.get(edge);
                App.getGamePanel().repaint();
            }
        }
        return closestTileEdge;
    }

    public TileVertex findClosestVertex() {
        closestTileVertex = new TileVertex();
        for (TileVertex v : vertices) {
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

    public void initialiseBoardAfterTransfer() {
        initialiseLayout();
        loadImages();
        tileImages = TileImageLoader.loadAndResizeTileImages(false);
        tileImagesS = TileImageLoader.loadAndResizeTileImages(true);
        initialiseVertices();
        initialiseEdges();
        initialiseBoardImage();
        initDiceValueImages();
        this.initialisePorts();
        loadHarborImage();
    }

    public void initialiseLayout() {
        double scaleFactorX = (double) Constants.Game.WIDTH / Constants.Game.BASE_WIDTH;
        double scaleFactorY = (double) Constants.Game.HEIGHT / Constants.Game.BASE_HEIGHT;
        System.out.println(scaleFactorX + " et " + scaleFactorY);
        Point point1 = new Point(
                (int) (267 * scaleFactorX),
                (int) (267 * scaleFactorY)
        );
        System.out.println((int) (267 * scaleFactorX) + " et  ; " + (int) (47 * scaleFactorX));
        Point point2 = new Point(
                (int) (47 * scaleFactorX),
                (int) (47 * scaleFactorY)
        );
//      Point point2 = new Point((int) (93 / Resolution.divider()), (int) (93 / Resolution.divider()));
        layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
    }


    public void changehighlitedTile(int tileId) {
        for (Tile tile : board.values()) {
            if (tile.getId() == tileId) {
                highlightedTile = tile;
            }
        }
    }

}
