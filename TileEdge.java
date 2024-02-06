public class TileEdge {
    private boolean hasRoad;

    public TileEdge() {
        this.hasRoad = false;
    }

    public boolean hasRoad() {
        return hasRoad;
    }   

    public void PlaceRoad(boolean hasRoad) {
        this.hasRoad = hasRoad;
    }

    public void RemoveRoad() {
        this.hasRoad = false;
    }

}