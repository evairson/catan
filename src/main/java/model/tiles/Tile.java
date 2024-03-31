package model.tiles;

import java.io.Serializable;

import model.geometry.CubeCoordinates;
import view.TileType;
import java.util.Set;
import java.util.HashSet;

public class Tile implements Serializable {
    private int q;
    private int r;
    private int diceValue;
    private TileType resourceType;
    private int id;
    private Set<TileEdge> edges;
    private Set<TileVertex> vertices;
    private static int idClass = 0;

    public Tile(int q, int r) {
        this.q = q;
        this.r = r;
        this.diceValue = 0;
        id = idClass;
        idClass++;
        edges = new HashSet<>();
        vertices = new HashSet<>();
    }

    public Tile(int q, int r, int diceValue) {
        this.q = q;
        this.r = r;
        this.diceValue = diceValue;
        id = idClass;
        idClass++;
        edges = new HashSet<>();
        vertices = new HashSet<>();
    }

    public Tile(int q, int r, int diceValue, TileType resourceType) {
        this.q = q;
        this.r = r;
        this.diceValue = diceValue;
        this.resourceType = resourceType;
        id = idClass;
        idClass++;
        edges = new HashSet<>();
        vertices = new HashSet<>();
    }

    public TileType getResourceType() {
        return resourceType;
    }

    public void setResourceType(TileType resourceType) {
        this.resourceType = resourceType;
    }

    public int getDiceValue() {
        return diceValue;
    }

    public void setDiceValue(int diceValue) {
        this.diceValue = diceValue;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public CubeCoordinates getCoordinates() {
        return new CubeCoordinates(q, r, -q - r);
    }

    public boolean equals(Tile t) {
        return (t.getQ() == q && t.getR() == r);
    }

    public String toString() {
        return "(" + q + ", " + r + ")";
    }

    public int getId() {
        return id;
    }

    public static void addIdClass() {
        idClass++;
    }

    public static int getIdClass() {
        return idClass;
    }

    public void addEdge(TileEdge edge) {
        edges.add(edge);
    }

    public void addVertex(TileVertex vertex) {
        vertices.add(vertex);
    }

    public Set<TileEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<TileEdge> edges) {
        this.edges = edges;
    }

    public Set<TileVertex> getVertices() {
        return vertices;
    }

    public void setVertices(Set<TileVertex> vertices) {
        this.vertices = vertices;
    }
}
