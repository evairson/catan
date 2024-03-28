package model.IA;

import java.util.HashMap;

import model.Player;
import model.Player.Color;
import view.TileType;

public class Bot extends Player {

    public Bot(Color c, String name, int id) {
        super(c, name, id);
    }

    public boolean acceptTrade(HashMap<TileType, Integer> resourcesRequested,
        HashMap<TileType, Integer> resourcesOffered) {
        return true;

    }
}
