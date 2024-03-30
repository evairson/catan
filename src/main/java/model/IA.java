package model;


import model.geometry.CubeCoordinates;
import model.tiles.Tile;
import model.tiles.TileVertex;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class IA {
    private double proportionWood;
    private double proportionWool;
    private double proportionOre;
    private double proportionWheat;
    private double proportionClay;

    public IA() {

    }

    public int diceValueToPoints(int diceValue) {
        switch (diceValue) {
            case 2: case 12: return 1;
            case 3: case 11: return 2;
            case 4: case 10: return 3;
            case 5: case 9: return 4;
            case 6: case 8: return 5;
            default: return 0;
        }
    }

    public double vertexToValue(TileVertex vertex) {
        double value = 0;
        for (Tile t : vertex.getTiles()) {
            switch (t.getResourceType()) { //juste la somme des dés
                case WOOD: value += diceValueToPoints(t.getDiceValue()); break;
                case WHEAT: value += diceValueToPoints(t.getDiceValue()); break;
                case WOOL: value += diceValueToPoints(t.getDiceValue()); break;
                case ORE: value += diceValueToPoints(t.getDiceValue()); break;
                case CLAY: value += diceValueToPoints(t.getDiceValue()); break;
                default: return 0;
            }
            /*
            switch (t.getResourceType()) {
                case WOOD: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionWood); break;
                case WHEAT: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionWheat); break;
                case WOOL: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionWool); break;
                case ORE: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionOre); break;
                case CLAY: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionClay); break;
                default: return 0;
            }*/
        }
        return value;
    }

    public void boardStats(ArrayList<Tile> board) {
        int diceValueOre = 0;
        int diceValueWheat = 0;
        int diceValueWool = 0;
        int diceValueClay = 0;
        int diceValueWood = 0;
        for (Tile t : board) {
            int points = diceValueToPoints(t.getDiceValue());
            switch (t.getResourceType()) {
                case WOOD: diceValueWood += points; break;
                case ORE: diceValueOre += points; break;
                case WHEAT: diceValueWheat += points; break;
                case WOOL: diceValueWool += points; break;
                case CLAY: diceValueClay += points; break;
                default: break;
            }
        }
        proportionClay = diceValueClay / 126; //somme du nombre associé à chaque tile
        proportionOre = diceValueOre / 126;
        proportionWheat = diceValueWheat / 126;
        proportionWood = diceValueWood / 126;
        proportionWool = diceValueWool / 126;
    }

    public TileVertex getBetterVertex(LinkedHashMap<CubeCoordinates, Tile> b, ArrayList<TileVertex> v) {
        ArrayList<Tile> board = new ArrayList<>(b.values());
        boardStats(board);
        double maxValue = 0;
        TileVertex maxVertex = null;
        for (TileVertex vertex : v) {
            for (Tile t : vertex.getTiles()) {
                System.out.println(t.getDiceValue());
            }
            double value = vertexToValue(vertex);
            System.out.println(value);
            if (value > maxValue) {
                maxValue = value;
                maxVertex = vertex;
            }
        }
        return maxVertex;
    }

}
