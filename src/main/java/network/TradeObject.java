package network;

import java.io.Serializable;
import java.util.HashMap;

import view.TileType;

public class TradeObject implements Serializable {
    private int idTrader;
    private int idPlayer;
    private HashMap<TileType, Integer> resourcesRequested;
    private HashMap<TileType, Integer> resourcesOffered;


    public TradeObject(int idTrader, int idPlayer, HashMap<TileType, Integer> resourcesRequested,
        HashMap<TileType, Integer> resourcesOffered) {
        this.idTrader = idTrader;
        this.idPlayer = idPlayer;
        this.resourcesOffered = resourcesOffered;
        this.resourcesRequested = resourcesRequested;
    }

    public int getIdPlayer() {
        return idPlayer;
    }
    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }
    public HashMap<TileType, Integer> getResourcesRequested() {
        return resourcesRequested;
    }
    public void setResourcesRequested(HashMap<TileType, Integer> resourcesRequested) {
        this.resourcesRequested = resourcesRequested;
    }
    public HashMap<TileType, Integer> getResourcesOffered() {
        return resourcesOffered;
    }
    public void setResourcesOffered(HashMap<TileType, Integer> resourcesOffered) {
        this.resourcesOffered = resourcesOffered;
    }

}
