package network;

import java.io.Serializable;
import java.util.HashMap;

import view.TileType;

public class TradeObject implements Serializable {
    private int idTrader;
    private int idPlayer;
    private HashMap<String, Integer> resourcesRequested;
    private HashMap<String, Integer> resourcesOffered;

    private boolean isOfferedDouble;
    private boolean isrequestedDouble;

    public TradeObject(int idTrader, int idPlayer, HashMap<String, Integer> resourcesRequested,
        HashMap<String, Integer> resourcesOffered, boolean isOfferedDouble, boolean isrequestedDouble) {
        this.idTrader = idTrader;
        this.idPlayer = idPlayer;
        this.resourcesOffered = resourcesOffered;
        this.resourcesRequested = resourcesRequested;
        this.isrequestedDouble = isrequestedDouble;
        this.isOfferedDouble = isOfferedDouble;
    }

    public int getIdPlayer() {
        return idPlayer;
    }
    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }
    public HashMap<String, Integer> getResourcesRequested() {
        return resourcesRequested;
    }
    public void setResourcesRequested(HashMap<String, Integer> resourcesRequested) {
        this.resourcesRequested = resourcesRequested;
    }
    public HashMap<String, Integer> getResourcesOffered() {
        return resourcesOffered;
    }
    public void setResourcesOffered(HashMap<String, Integer> resourcesOffered) {
        this.resourcesOffered = resourcesOffered;
    }

    public boolean isOfferedDouble() {
        return isOfferedDouble;
    }

    public boolean isrequestedDouble() {
        return isrequestedDouble;
    }

    public static HashMap<String, Integer> toString(HashMap<TileType, Integer> typeMap) {
        HashMap<String, Integer> stringKeyMap = new HashMap<>();

        for (HashMap.Entry<TileType, Integer> entry : typeMap.entrySet()) {
            stringKeyMap.put(entry.getKey().name(), entry.getValue());
        }
        return stringKeyMap;
    }

    public static HashMap<TileType, Integer> toTileType(HashMap<String, Integer> stringKeyMap) {
        HashMap<TileType, Integer> typeMap = new HashMap<>();

        for (HashMap.Entry<String, Integer> entry : stringKeyMap.entrySet()) {
            typeMap.put(TileType.valueOf(entry.getKey()), entry.getValue());
        }
        return typeMap;
    }

}
