public class Tile {
    private int tileType;
    private int number;
    private boolean hasRobber;
    private TileVertex[] vertices;
    private TileEdge[] edges;
    private CubeCoordinates coordinates;

    public Tile(int tileType, int number, boolean hasRobber, CubeCoordinates coordinates) {
        this.tileType = tileType;
        this.number = number;
        this.hasRobber = hasRobber;
        this.coordinates = coordinates;
        this.vertices = new TileVertex[6];
        this.edges = new TileEdge[6];
        for (int i = 0; i < 6; i++) {
            this.vertices[i] = new TileVertex();
            this.edges[i] = new TileEdge();
        }
    }

    public Tile(int tileType, int number,CubeCoordinates coordinates) {
        this(tileType, number, false, coordinates);
    }

    public int getTileType() {
        return tileType;
    }

    public int getNumber() {
        return number;
    }

    public boolean hasRobber() {
        return hasRobber;
    }

    public CubeCoordinates getCoordinates() {
        return coordinates;
    }

    public TileVertex getVertex(int index) {
        return vertices[index];
    }

    public TileEdge getEdge(int index) {
        return edges[index];
    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public void setCoordinates(CubeCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setVertex(int index, TileVertex vertex) {
        this.vertices[index] = vertex;
    }

    public void setEdge(int index, TileEdge edge) {
        this.edges[index] = edge;
    }

    public String toString() {
        return "Tile Type: " + tileType + " Number: " + number + " Coordinates: " + coordinates;
    }
}
