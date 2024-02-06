public class TileVertex {
    private boolean hasSettlement;
    private boolean hasCity;

    public TileVertex() {
        this.hasSettlement = false;
        this.hasCity = false;
    }

    public boolean hasSettlement() {
        return hasSettlement;
    }

    public boolean hasCity() {
        return hasCity;
    }

    public boolean PlaceSettlement() {
        if (hasSettlement == false && hasCity == false) {
            hasSettlement = true;
            return true;
        }
        return false;
    }

    public boolean PlaceCity(boolean hasCity) {
        if (hasSettlement == true && hasCity == false) {
            this.hasCity = hasCity;
            return true;
        }
        return false;
    }

    public void RemoveSettlement() {
        hasSettlement = false;
    }

    public void RemoveCity() {
        hasCity = false;
    }

    public void RemoveAll() {
        hasSettlement = false;
        hasCity = false;
    }

    
}
