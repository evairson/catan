package model.tiles;

import java.io.Serializable;
import model.buildings.Harbor;
import model.geometry.Point;
import java.util.HashSet;
import java.util.Set;
import model.buildings.Building;

/**
 * TileVertex
 * This class represents a vertex of a tile in a hexagonal grid.
 * A tile vertex is a point in the grid. it represents the vertex of a tile in the game.
 * it has a set of tiles that are connected to the vertex, a building, an id, and a harbor.
 * the building placed on the vertex can only be a settlement (colony) or a city.
 * the harbor is the harbor that is connected to the vertex, the harbor is null if there is no harbor.
 */
public class TileVertex implements Serializable {
    private Set<Tile> tiles;
    private Building building;
    private static int idClass;
    private int id;
    private Harbor harbor;
    private Point coordinates;

  
    public TileVertex() {
        tiles = new HashSet<>();
    }

    public static int getIdClass() {
        return idClass;
    }

    public static void addIdClass() {
        idClass++;
    }

    public static void resetIdClass() {
        idClass = 0;
    }

    public void setHarbor(Harbor harbor) {
        this.harbor = harbor;
    }

    public Harbor getHarbor() {
        return harbor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void addTile(Tile tile) {
        tiles.add(tile);
        tile.addVertex(this);
    }
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }
    public Point getCoordinates() {
        return coordinates;
    }
    public void setBuilding(Building building) {
        this.building = building;
    }
    public Building getBuilding() {
        return building;
    }
    public Set<Tile> getTiles() {
        return tiles;
    }
}
