package model;

import model.bots.Bot;
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

/**
 * GameBoard
 * This class represents the game board.
 * The game board is a hexagonal grid of tiles.
 * It has a layout, a grid size, a set of tiles, a set of vertices, a set of edges, a thief, and a thief mode.
 * The layout is the layout of the grid.
 * The grid size is the size of the grid.
 * The set of tiles is the set of tiles in the grid.
 * The set of vertices is the set of vertices in the grid.
 * The set of edges is the set of edges in the grid.
 * The thief is the thief that is on the board.
 * The thief mode is a boolean that indicates if the game is in thief mode.
 */

public class GameBoard implements Serializable {

    private LinkedHashMap<CubeCoordinates, Tile> board;
    private Map<TileType, BufferedImage> specializedHarborImages;
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
    private boolean shadowHexes = false;

    private TileVertex lastPlacedColonyVertex;

    private TileVertex closestTileVertex = new TileVertex();
    private TileEdge closestTileEdge = new TileEdge();

    private Tile highlightedTile;
    private Map<String, BufferedImage> loadedImages;

    private Map<Integer, BufferedImage> diceValueImages = new HashMap<>();

    private BufferedImage boardImage;
    private BufferedImage roadImage;
    private BufferedImage scaledRoadImage;

    private static Font baseFont = new Font("SansSerif", Font.BOLD,
            (int) (30 / Constants.Game.DIVIDER));
    private static Font highlightedFont = new Font("SansSerif",
            Font.BOLD, (int) (35 / Constants.Game.DIVIDER));

    private Map<Building, Image> scaledBuildingImages = new HashMap<>();
    private BufferedImage harborImage;
    private BufferedImage standardHarborImage;

    public GameBoard(Thief thief, Game game) {

        this.thief = thief;

        board = new LinkedHashMap<CubeCoordinates, Tile>();
        this.game = game;
        this.initialiseBoard();
    }

    public GameBoard(HashMap<Point, TileEdge> edge, Game game) {
        edgesMap = edge;
        this.game = game;
    }

    // Méthodes pour s'occuper des ports
    private void loadHarborImage() {
        try {
            BufferedImage originalImage = ImageIO.read(new File("src/main/resources/harbor.png"));
            // Ajustez la taille comme nécessaire
            int width = originalImage.getWidth() / 5;
            int height = originalImage.getHeight() / 5;
            standardHarborImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = standardHarborImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();

            specializedHarborImages = new HashMap<>();
            for (TileType type : TileType.values()) {
                originalImage = ImageIO.read(new File(type.getSpecializedImagePath()));
                width = originalImage.getWidth() / 5;
                height = originalImage.getHeight() / 5;
                BufferedImage specializedImage = new BufferedImage(width,
                        height, BufferedImage.TYPE_INT_ARGB);
                g2d = specializedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, width, height, null);
                g2d.dispose();
                specializedHarborImages.put(type, specializedImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawPorts(Graphics g) {
        for (Map.Entry<TileVertex, Harbor> entry : harborMap.entrySet()) {
            TileVertex vertex = entry.getKey();
            Point location = calculatePortPosition(vertex.getCoordinates());
            drawRoad(g, vertex.getCoordinates(), location);

            if (entry.getValue() instanceof SpecializedHarbor) {
                SpecializedHarbor specializedHarbor = (SpecializedHarbor) entry.getValue();
                harborImage = specializedHarborImages.get(specializedHarbor.getResourceType());
            } else {
                harborImage = standardHarborImage;
            }

            g.drawImage(harborImage, (int) location.getX() - harborImage.getWidth() / 2,
                    (int) location.getY() - harborImage.getHeight() / 2, null);
        }
    }

    private Point calculatePortPosition(Point originalPoint) {
        Point center = new Point(400, 400);
        double angle = Math.atan2(originalPoint.getY() - center.getY(), originalPoint.getX() - center.getX());
        double distance = 50;

        int newX = (int) (originalPoint.getX() + distance * Math.cos(angle));
        int newY = (int) (originalPoint.getY() + distance * Math.sin(angle));

        return new Point(newX, newY);
    }

    private void loadRoadImage() {
        try {
            BufferedImage originalImage = ImageIO.read(new File("src/main/resources/roadNeutral.png"));
            int width = originalImage.getWidth() / 5;
            int height = originalImage.getHeight() / 5;
            roadImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roadImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();

            // Redimensionner l'image de la route
            int scaledWidth = (int) (roadImage.getWidth() * 1.2 / Constants.Game.DIVIDER);
            int scaledHeight = (int) (roadImage.getHeight() * 1.2 / Constants.Game.DIVIDER);
            Image tmp = roadImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            scaledRoadImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            g2d = scaledRoadImage.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawRoad(Graphics g, Point start, Point end) {
        double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX());
        angle += Math.PI / 2;
        int midX = (int) ((start.getX() + end.getX()) / 2);
        int midY = (int) ((start.getY() + end.getY()) / 2);

        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform transform = new AffineTransform();
        transform.translate(midX, midY);
        transform.rotate(angle);
        transform.translate((double) -scaledRoadImage.getWidth() / 2,
                (double) -scaledRoadImage.getHeight() / 2);
        g2d.drawImage(scaledRoadImage, transform, null);
        g2d.dispose();
    }

    public void initialisePorts() {
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

        // initialiser les ports classiques
        for (Point point : classicPortsPoints) {
            TileVertex harborVertex = findTileVertexByPoint(point);
            if (harborVertex != null) {
                Harbor port = new Harbor(harborVertex);
                harborVertex.setHarbor(port);
                harborMap.put(harborVertex, port);
            }
        }

        // initialiser les ports spécialisés
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

    public void setShadowHexes(boolean b) {
        shadowHexes = b;
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

    /**.
     * getNeighbourTileVerticesToVertex
     *
     * @param vertex this methods goes through the edges of the board.
     * once it finds the edges that contain the vertex on one of their ends, it adds them to an arraylist.
     * then it goes through the arraylist and adds the other vertex for every edge to another arraylist.
     * it returns the arraylist of vertices.
     * @return the three tile vertices that are closest to the given vertex
     * the three tile vertices are the ones that are connected to the given vertex by an edge
     * the three tile vertices are returned in an array
     */
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

    /**.
     * checkIfNeighbourInArray
     *
     * @param neighbours
     * @param vertex
     * @return true if the vertex is in the array of neighbours, false otherwise
     * this method is to check so that we don't add the same vertex to the array of neighbours multiple times
     */
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

    /**.
     * checkIfNeighbourInArray (overloaded method)
     *
     * @param neighbours
     * @param edge
     * @return true if the edge is in the array of neighbours, false otherwise
     * this method is to check so that we don't add the same edge to the array of neighbours multiple times
     */
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

    /**.
     * getNeighbourTileVerticesToEdge
     *
     * @param edge
     * @return the two tile vertices that are connected to the edge
     * that's basically used to find the two tile vertices that have
     * the same coordinates as the start and end of the edge
     * the two tile vertices are returned in an array
     */
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

    /**.
     * getNeighbourTileEdgesToEdge
     *
     * @param edge this method goes through the edges of the board.
     *             once it finds the edges that are connected to the given edge, it adds them to an arraylist.
     *             it returns the arraylist of edges.
     * @return the four tile edges that are connected to the given edge
     * the four tile edges are the ones that are connected to the given edge by a vertex
     * the four tile edges are returned in an array
     */

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

    /**.
     * getNeighbourTileEdgesToVertex
     *
     * @param vertex This method goes through the edges of the board.
     * once it finds the edges that contain the vertex on one of their ends,
     * -it adds them to an arraylist.
     * -it returns the arraylist of edges.
     * @return the three tile edges that are closest to the given vertex
     * the three tile edges are the ones that are connected to the given vertex by a vertex
     * the three tile edges are returned in an array
     */
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


    /**.
     * isVertexTwoRoadsAwayFromCities
     *
     * @param vertex this method goes through the edges of the board.
     * once it finds the edges that contain the vertex on one of their ends, it adds them to an arraylist.
     * then it goes through the arraylist and checks if the vertex is two roads away from cities.
     * basically, if a vertex one road away from a city contains a building,
     * then the vertex is not two roads away from cities.
     * @return true if the vertex is two roads away from cities, false otherwise
     * that's used to check if a player can place a colony on a vertex.
     * In the game, a player can only place a colony on a vertex if it is two roads away from cities.
     * @see #getNeighbourTileVerticesToVertex to see how the vertices are found.
     */
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

    /**.
     * isRoadNextToCity
     *
     * @param edge
     * @param player this method goes through the vertices of the board.
     * once it finds the vertices that are connected to the edge, it checks if the vertex contains a building.
     * if the vertex contains a building, then it checks if the building belongs to the player.
     * @return true if the road is next to a city that belongs to the player, false otherwise
     * that's used to check if a player can place a road on an edge.
     * In the game, a player can only place a road on an edge
     * if it is next to a city that belongs to the player.
     * that's also used to check if a player can place a road
     * on an edge if it is next to a road that belongs to the player.
     * That rule is always true except for the first two roads that the player places.
     * @see #getNeighbourTileVerticesToEdge to see how the vertices are found.
     */
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

    /**.
     * isRoadNextToVertex
     *
     * @param edge
     * @param vertex this method goes through the vertices of the board.
     * once it finds the vertices that are connected to the edge,
     * it checks if the vertex is the same as the given vertex.
     * @return true if the road is next to the given vertex, false otherwise
     * that's used to check if a player can place a city on a vertex.
     * In the game, a player can only place a city or colony
     * on a vertex if it is next to a road that belongs to the player.
     * The only exception is when the player places the first two roads and colonies.
     * @see #getNeighbourTileVerticesToEdge to see how the vertices are found.
     */
    public boolean isRoadNextToVertex(TileEdge edge, TileVertex vertex) {
        TileVertex[] neighbours = getNeighbourTileVerticesToEdge(edge);
        for (TileVertex v : neighbours) {
            if (v != null) {
                if (v == vertex) {
                    return true;
                }
            }
        }
        return false;
    }

    /**.
     * isRoadNextToRoad
     *
     * @param edge   the edge that is checked if it is next to a road
     * @param player the player that is checked if the road belongs to him
     * this method goes through the edges of the board.
     * it then checks if the edge contains a building.
     * if the edge contains a building, then it checks if the building belongs to the player.
     * @return true if the road is next to a road that belongs to the player, false otherwise
     * that's used to check if a player can place a road on an edge.
     * In the game, a player can only place a road
     * on an edge if it is next to a road or a colony that belongs to the player.
     * this method in particular is used to check if
     * a player can place a road on an edge if it is next to a road that belongs to the player.
     * @see #getNeighbourTileEdgesToEdge to see how the edges are found.
     */
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


    /**.
     * isVertexNextToRoad
     *
     * @param vertex the vertex that is checked if it is next to a road
     * @param player the player that is checked if the road belongs to him
     * this method goes through the edges of the board.
     * once it finds the edges that are connected to the vertex,
     * it checks if the edge contains a building.
     * if the edge contains a building, then it checks if the building belongs to the player.
     * @return true if the vertex is next to a road that belongs to the player, false otherwise
     * that's used to check if a player can place a colony on a vertex.
     * In the game, a player can only place a colony on a vertex
     * if it is next to a road that belongs to the player.
     * that's also used to check if a player can place a colony
     * on a vertex if it is next to a city that belongs to the player.
     * @see #getNeighbourTileEdgesToVertex to see how the edges are found.
     */
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

    /**.
     * canPlaceColony
     *
     * @param vertex the vertex where the player wants to place a colony
     * @param player the player that wants to place a colony
     * @return true if the player can place a colony on the vertex, false otherwise
     * that's used to check if a player can place a colony on a vertex.
     * it also checks if the player is allowed to place a colony for free.
     * it's also checking if the vertex already contains a building.
     * In the game, a player can only place a colony on a vertex if:
     * it is two roads away from cities,
     * and next to a road that belongs to the player (except for the first two roads and colonies).
     * @see #isVertexTwoRoadsAwayFromCities
     * to see how the vertex is checked if it is two roads away from cities.
     * @see #isVertexNextToRoad to see how the
     * vertex is checked if it is next to a road that belongs to the player.
     */
    public boolean canPlaceColony(TileVertex vertex, Player player) {
        if (!isVertexTwoRoadsAwayFromCities(vertex)) {
            return false;
        }
        if (vertex.getBuilding() != null) {
            return false;
        }
        if (game.getCurrentPlayer().getFreeColony()) {
            lastPlacedColonyVertex = vertex;
            return true;
        }
        if (!isVertexNextToRoad(vertex, player)) {
            return false;
        }
        return true;
    }

    /**.
     * canPlaceCity
     *
     * @param vertex
     * @param player it's used to check if we have the conditions to place a city
     * @return true if we can, false otherwise
     * @see Colony to see how we create cities or colonies
     */
    public boolean canPlaceCity(TileVertex vertex, Player player) {
        if (!(vertex.getBuilding() instanceof Colony)) {
            return false;
        } else {
            Building buil = vertex.getBuilding();
            if (buil instanceof Colony) {
                Colony col = (Colony) buil;
                if (!col.getIsCity()) {
                    if (col.getOwner() == player) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**.
     * canPlaceRoad
     *
     * @param edge   the edge where the player wants to place a road
     * @param player the player that wants to place a road
     * @return true if the player can place a road on the edge, false otherwise
     * that's used to check if a player can place a road on an edge.
     * it also checks if the player is allowed to place a road for free.
     * it's also checking if the edge already contains a building.
     * In the game, a player can only place a road on an edge if:
     * it is next to a city that belongs to the
     * player, or next to a road that belongs to the player (except for the first two roads).
     * @see #isRoadNextToCity to see how the edge
     * is checked if it is next to a city that belongs to the player.
     * @see #isRoadNextToRoad to see how the edge
     * is checked if it is next to a road that belongs to the player.
     */
    public boolean canPlaceRoad(TileEdge edge, Player player) {
        if (edge.getBuilding() != null) {
            return false;
        }
        if (lastPlacedColonyVertex != null && game.getCurrentPlayer().getFreeRoad() > 0) {
            if (isRoadNextToVertex(edge, lastPlacedColonyVertex)) {
                lastPlacedColonyVertex = null;
                return true;
            }
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

    /**.
     * addTile
     *
     * @param q            the first coordinate of the tile
     * @param r            the second coordinate of the tile
     * @param diceValue    the dice value of the tile
     * @param resourceType the resource type of the tile
     *                     this method adds a tile to the board.
     *                     it creates a tile with the given q, r, dice value, and resource type.
     *                     it then adds the tile to the board.
     *                     if the resource type is desert, then the thief is set to the tile.
     * @see #getTileType to see how the resource type is converted to a tile type.
     * @see Thief to see how the thief is set to the tile.
     * @see Tile to see how the tile is created.
     * @see CubeCoordinates to see how the tile is added to the board.
     */
    public void addTile(int q, int r, int diceValue, TileType resourceType) {
        int s = -q - r;
        Tile tile = new Tile(q, r, diceValue, resourceType);
        if (resourceType == TileType.DESERT) {
            thief.setTile(tile);
        }
        board.put(new CubeCoordinates(q, r, s), tile);
    }

    /**.
     * getTile
     *
     * @param q
     * @param r
     * @return the tile at the given q, r coordinates
     * this method returns the tile at the given q, r coordinates.
     * @see CubeCoordinates to see how the tile is found
     * in the board, and how the q, r, s coordinates are used.
     */
    public Tile getTile(int q, int r) {
        int s = -q - r;
        return board.get(new CubeCoordinates(q, r, s));
    }

    public LinkedHashMap<CubeCoordinates, Tile> getBoard() {
        return board;
    }


    /**
     * initialiseBoard
     * this method initialises the board.
     * it goes through the grid size and creates tiles for each q, r, s coordinates.
     * it then adds the tiles to the board.
     * it also sets the dice value and resource type for each tile.
     * the dice value and resource type are set based on the preset tile dice values and tile resource types.
     * the preset tile dice values and tile resource types are constants.
     *
     * @see Constants.BoardConstants#TILE_DICE_VALUES to see the preset tile dice values.
     * @see Constants.BoardConstants#TILE_TYPES to see the preset tile resource types.
     * @see #addTile to see how the tiles are added to the board.
     * @see #getTileType to see how the resource type is converted to a tile type.
     * @see Tile to see how the tile is created.
     * @see CubeCoordinates to see how the tile is added to the board.
     */
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

    public void modifyDiceValue(int q, int r, int diceValue) {
        int s = -q - r;
        board.get(new CubeCoordinates(q, r, s)).setDiceValue(diceValue);
    }

    public void exchangeTwelveTwoToEightSix() {
        for (Map.Entry<CubeCoordinates, Tile> entry : board.entrySet()) {
            Tile tile = entry.getValue();
            if (tile.getDiceValue() == 12) {
                tile.setDiceValue(8);
            } else if (tile.getDiceValue() == 2) {
                tile.setDiceValue(6);
            } else if (tile.getDiceValue() == 8) {
                tile.setDiceValue(12);
            } else if (tile.getDiceValue() == 6) {
                tile.setDiceValue(2);
            } else if (tile.getDiceValue() == 3) {
                tile.setDiceValue(9);
            } else if (tile.getDiceValue() == 9) {
                tile.setDiceValue(3);
            } else if (tile.getDiceValue() == 4) {
                tile.setDiceValue(10);
            } else if (tile.getDiceValue() == 10) {
                tile.setDiceValue(4);
            } else if (tile.getDiceValue() == 5) {
                tile.setDiceValue(11);
            } else if (tile.getDiceValue() == 11) {
                tile.setDiceValue(5);
            }
        }
    }

    public void eventChangeDiceValues() {
        exchangeTwelveTwoToEightSix();
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

    /**
     * initDiceValueImages
     * this method initialises the dice value images.
     * it goes through the possible dice values and creates an image for each dice value.
     * it then draws the dice value on the image.
     * the dice value is drawn in the center of the image.
     * the color of the dice value is red if the dice value is 6 or 8, and black otherwise.
     *
     * @see Constants.Game#DIVIDER to see how the font size is divided.
     * @see #baseFont to see the font used to draw the dice value.
     * @see #diceValueImages to see how the dice value images are stored.
     * @see BufferedImage to see how the dice value images are created.
     * @see Graphics2D to see how the dice value images are drawn.
     * @see FontMetrics to see how the dice value is centered.
     * @see Color to see how the color of the dice value is set.
     */
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

    /**.
     * getTileType
     *
     * @param resourceType the type of resource that the tile contains
     * @return the tile type based on the resource type
     * this method converts the resource type to a tile type.
     * the resource type is an integer that represents the type of resource that the tile contains.
     * the tile type is an enum that represents the type of resource that the tile contains.
     * @see TileType to see the enum that represents the type of resource that the tile contains.
     */
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

    /**
     * initialiseVertices
     * this method initialises the vertices.
     * it goes through the tiles of the board.
     * for each tile, it gets the coordinates of the vertices of the tile.
     * for each vertex of the tile, it checks if the vertex is already in the map.
     * if the vertex is already in the map,
     * then the tile is added to the list of tiles associated with the vertex.
     * if the vertex is not in the map, then a new tile vertex object is created for the vertex.
     * the tile is then added to the list of tiles associated with the vertex.
     * the vertex is then added to the map with the tile vertex object as the value.
     *
     * @see TileVertex to see the class that represents the vertex.
     * @see Tile to see the class that represents the tile.
     * @see CubeCoordinates to see how the coordinates of the vertices are obtained.
     * @see #layout to see how the coordinates of the vertices are obtained.
     */
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

    /**.
     * initialiseEdges
     * this method initialises the edges.
     * it goes through the tiles of the board.
     * for each tile, it gets the coordinates of the edges of the tile.
     * for each edge of the tile, it checks if the edge is already in the map.
     * if the edge is already in the map,
     * then the tile is added to the list of tiles associated with the edge.
     * if the edge is not in the map, then a new tile edge object is created for the edge.
     * the tile is then added to the list of tiles associated with the edge.
     * the edge is then added to the map with the tile edge object as the value.
     *
     * @see TileEdge to see the class that represents the edge.
     * @see Tile to see the class that represents the tile.
     * @see CubeCoordinates to see how the coordinates of the edges are obtained.
     * @see #layout to see how the coordinates of the edges are obtained.
     */
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

    /**.
     * edgesMapContainsEdge
     *
     * @param edge the edge we want to check if it is in the map
     * @return true if the edge is already in the map, false otherwise
     * this method checks if the given edge is already in the map.
     * that's used to avoid adding the same edge multiple times to the map.
     */
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

    /**.
     * buildingIsCity
     *
     * @param building the building
     * @return true if the building is a city, false otherwise (it can be a colony or null)
     */
    private boolean buildingIsCity(Building building) {
        if (building instanceof Colony) {
            return ((Colony) building).getIsCity();
        }
        return false;
    }

    /**.
     * drawBuildingImage
     *
     * @param g2d      the graphics object
     * @param building the building
     * @param vertex   the vertex we want to draw the building on
     *                 this method draws the building image on the vertex.
     *                 it checks if the building is a city or a colony.
     *                 if the building is a city, then the city image is drawn on the vertex.
     *                 if the building is a colony, then the colony image is drawn on the vertex.
     * @see #buildingIsCity to see how the building is checked if it is a city.
     * @see #getImageForBuilding to see how the image for the building is obtained.
     * @see #scaledBuildingImages to see how the image for the building is scaled.
     * @see Resolution#divider to see how the image is scaled based on the resolution.
     * @see Point to see how the building image is drawn on the vertex.
     * @see Graphics2D to see how the building image is drawn on the vertex.
     */
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

    /**.
     * drawVertices
     *
     * @param g the graphics object
     *          this method draws the vertices on the board.
     *          it goes through the vertices of the board.
     *          for each vertex, it checks if the vertex contains a building.
     *          if the vertex contains a building, then the building image is drawn on the vertex.
     * @see #drawBuildingImage to see how the building image is drawn on the vertex.
     */
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

    /**.
     * drawImagesInHexes
     *
     * @param g the graphics object
     *          this method draws the images in the hexes.
     *          it goes through the tiles of the board.
     *          for each tile, it gets the pixel coordinates of the tile.
     *          it then draws the image of the tile on the pixel coordinates.
     *          if the game is in thief mode, then the image of the selected tile is highlighted.
     */
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

    /**.
     * drawEdgeWithImage
     *
     * @param g2d       the graphics object
     * @param start     the start point of the edge
     * @param end       the end point of the edge
     * @param edgeImage the image of the edge
     *                  this method draws the edge with the image.
     *                  it calculates the middle of the edge and the rotation angle.
     *                  it scales the image of the edge.
     *                  it then applies the transformation and draws the image.
     * @see AffineTransform to see how the transformation is applied.
     * @see #drawImagesInHexes to see how the images are drawn in the hexes.
     * @see #drawVertices to see how the vertices are drawn.
     * @see #drawPorts to see how the ports are drawn.
     * @see #drawThief to see how the thief is drawn.
     */
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


    /**.
     * drawEdges
     *
     * @param g the graphics object
     *          this method draws the edges on the board.
     *          it goes through the edges of the board.
     *          for each edge, it checks if the edge contains a building.
     *          if the edge contains a building, then the edge image is drawn on the edge.
     * @see #drawEdgeWithImage to see how the edge image is drawn on the edge.
     */
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

    /**.
     * drawPorts
     *
     * @param g the graphics object
     *          this method draws the ports on the board.
     *          it goes through the ports of the board.
     *          for each port, it gets the pixel coordinates of the port.
     *          it then draws the port image on the pixel coordinates.
     * @see #drawBuildingImage to see how the building image is drawn on the vertex.
     * @see #drawEdgeWithImage to see how the edge image is drawn on the edge.
     * @see #drawImagesInHexes to see how the images are drawn in the hexes.
     * @see #drawVertices to see how the vertices are drawn.
     * @see #drawThief to see how the thief is drawn.
     * @see #drawEdges to see how the edges are drawn.
     * @see #drawPorts to see how the ports are drawn.
     * @see #draw to see how the board is drawn.
     */
    public void drawBoard(Graphics g) {
        if (!shadowHexes) {
            drawImagesInHexes(g);
        }
        drawPorts(g);
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

    /**.
     * draw
     *
     * @param g the graphics object
     *          this method draws the board.
     *          it draws the images in the hexes, the edges, the vertices, the ports, and the thief.
     * @see #drawImagesInHexes to see how the images are drawn in the hexes.
     * @see #drawVertices to see how the vertices are drawn.
     * @see #drawPorts to see how the ports are drawn.
     * @see #drawThief to see how the thief is drawn.
     * @see #drawEdges to see how the edges are drawn.
     * @see #drawBuildingImage to see how the building image is drawn on the vertex.
     * @see #drawEdgeWithImage to see how the edge image is drawn on the edge.
     * @see #drawImagesInHexes to see how the images are drawn in the hexes.
     */
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


    /**.
     * drawThief
     * @param g the graphics object
     * this method draws the thief on the board.
     * it gets the pixel coordinates of the tile where the thief is.
     * it then draws the thief image on the pixel coordinates.
     * @see #layout to see how the pixel coordinates of the tile are obtained.
     * @see Thief to see how the thief is set to the tile.
     * @see Graphics2D to see how the thief image is drawn.
     * @see Color to see the color of the thief image.
     * @see Graphics2D#fillOval  to see how the thief image is drawn.
     * @see #draw to see how the board is drawn.
     */
    public void drawThief(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Point p = layout.cubeToPixel(layout, thief.getTile().getCoordinates());
        g2d.setColor(Color.black);
        g2d.fillOval((int) p.getX() - 10, (int) p.getY() - 10, 20, 20);
    }

    /**.
     * mouseMoved
     * @param e the mouse event
     * this method is called when the mouse is moved.
     * it gets the mouse position.
     * it then finds the closest vertex and the closest edge to the mouse position.
     * if the game is in thief mode, then the tile that is closest to the mouse position is highlighted.
     * @see #findClosestVertex to see how the closest vertex is found.
     * @see #findClosestEdge to see how the closest edge is found.
     * @see #changeThief to see how the thief is changed.
     */
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
        if (highlightedTile.getId() == thief.getTile().getId()) {
            return;
        }

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
        highlightedTile = ((Bot) game.getCurrentPlayer()).getThiefTile(game);
        changeThief();
    }

    public void changeThief() {
        thief.setTile(highlightedTile);
        game.setThiefMode(false);
        thiefMode = false;
        App.getGamePanel().repaint();
        App.getActionPlayerPanel().update();
    }

    /**.
     * findClosestEdge
     * @return the closest edge to the mouse position
     * this method finds the closest edge to the mouse position.
     * it goes through the edges of the board.
     * for each edge, it calculates the distance between the edge and the mouse position.
     * if the distance is less than the minimum distance to the edge,
     * then the minimum distance to the edge is updated.
     * the closest edge is then updated.
     * @see TileEdge to see the class that represents the edge.
     * @see Point to see the class that represents the coordinates.
     */
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

    /**.
     * findClosestVertex
     * @return the closest vertex to the mouse position
     * this method finds the closest vertex to the mouse position.
     * it goes through the vertices of the board.
     * for each vertex, it calculates the distance between the vertex and the mouse position.
     * if the distance is less than the minimum distance to the vertex,
     * then the minimum distance to the vertex is updated.
     * the closest vertex is then updated.
     * @see TileVertex to see the class that represents the vertex.
     * @see Point to see the class that represents the coordinates.
     */
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
        loadRoadImage();
    }

    /**.
     * initialiseLayout
     * this method initialises the layout of the board.
     * it creates a layout with the pointy orientation and the point1 and point2 coordinates.
     * the point1 and point2 coordinates are scaled based on the game width and height.
     * @see Constants.Game to see how the game width and height are used to scale the coordinates.
     * @see Layout to see how the layout is created.
     * @see Point to see how the coordinates are scaled.
     * @see Constants.OrientationConstants to see the pointy orientation.
     */
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
