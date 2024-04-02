package model.IA;

import java.util.ArrayList;
import java.util.HashMap;

import exceptionclass.ConstructBuildingException;
import model.Game;
import model.Player;
import model.Player.Color;
import model.tiles.Tile;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import view.TileType;

public class Bot extends Player {
    private static double proportionWood;
    private static double proportionWool;
    private static double proportionOre;
    private static double proportionWheat;
    private static double proportionClay;


    public Bot(Color c, String name, int id) {
        super(c, name, id);
    }

    public boolean acceptTrade(HashMap<TileType, Integer> resourcesRequested,
        HashMap<TileType, Integer> resourcesOffered) {
        return true;

    }

    public static int diceValueToPoints(int diceValue) {
        switch (diceValue) {
            case 2: case 12: return 1;
            case 3: case 11: return 2;
            case 4: case 10: return 3;
            case 5: case 9: return 4;
            case 6: case 8: return 5;
            default: return 0;
        }
    }

    public static double vertexToValue(TileVertex vertex) {
        double value = 0;
        for (Tile t : vertex.getTiles()) {
            /*switch (t.getResourceType()) { //juste la somme des dés
                case WOOD: value += diceValueToPoints(t.getDiceValue()); break;
                case WHEAT: value += diceValueToPoints(t.getDiceValue()); break;
                case WOOL: value += diceValueToPoints(t.getDiceValue()); break;
                case ORE: value += diceValueToPoints(t.getDiceValue()); break;
                case CLAY: value += diceValueToPoints(t.getDiceValue()); break;
                default: return 0;
            }
            */
            switch (t.getResourceType()) {
                case WOOD: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionWood); break;
                case WHEAT: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionWheat); break;
                case WOOL: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionWool); break;
                case ORE: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionOre); break;
                case CLAY: value += diceValueToPoints(t.getDiceValue()) * (1 - proportionClay); break;
                default: return 0;
            }
        }
        return value;
    }

    public static void boardStats(ArrayList<Tile> board) {
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

    public static TileVertex getBetterVertex(Game game) {
        ArrayList<Tile> board = new ArrayList<>(game.getBoard().getBoard().values());
        boardStats(board);
        double maxValue = 0;
        TileVertex maxVertex = null;
        for (TileVertex vertex : game.getBoard().getVertices()) {
            if (vertex.getBuilding() != null) {
                continue;
            }
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

    public static TileEdge getRoadNext(Game game, TileVertex vertex) {
        for (TileEdge edge: game.getBoard().getEdgeMap().values()) {
            if (edge.getBuilding() != null) {
                continue;
            }
            if (edge.getStart().getX() == vertex.getCoordinates().getX()
                && edge.getStart().getY() == vertex.getCoordinates().getY()
                || edge.getEnd().getX() == vertex.getCoordinates().getX()
                && edge.getEnd().getY() == vertex.getCoordinates().getY()) {
                return edge;
            }
        }
        return null;
    }

    public void buildBestRoad(Game game) {
        TileEdge edge = getBestBeforeRoad(game);
        System.out.println(edge);
        for (TileEdge edgePossible : game.getBoard().getEdgeMap().values()) {
            if (edgePossible.getBuilding() != null || edgePossible == edge) {
                continue;
            }
            if (edgePossible.getEnd().distance(edge.getStart()) == 0
                || edgePossible.getStart().distance(edge.getStart()) == 0) {
                try {
                    game.buildRoad(edgePossible.getId());
                    return;
                } catch (ConstructBuildingException e) {
                    ConstructBuildingException.messageError();
                }
            }
        }
    }

    public TileEdge getBestBeforeRoad(Game game) {
        int numbersRoadsMax = 0;
        TileEdge edgeLastMax = null;
        for (TileEdge edge: game.getBoard().getEdgeMap().values()) {
            if (edge.getBuilding() != null && edge.getBuilding().getOwner().getId() == id) {
                if (edgeLastMax == null) {
                    edgeLastMax = edge;
                }
                if (edge.getNumberEdgesBefore(game) == 0) {
                    System.out.println("y'en a pas avant");
                    int numberRoads = game.getNumberRoads(edge, id);
                    TileEdge edgeLast = game.getRoadMax(edge, id);
                    if (numberRoads > numbersRoadsMax) {
                        numbersRoadsMax = numberRoads;
                        edgeLastMax = edgeLast;
                    }
                }
            }
        }

        System.out.println(numbersRoadsMax);
        return edgeLastMax;
    }


    public Tile getThiefTile(Game game) {
        Tile bestTile = null;
        for (Tile tile : game.getBoard().getBoard().values()) {
            if (bestTile == null) {
                bestTile = tile;
            }
            if (getNumberColoniesEnnemies(tile) > getNumberColoniesEnnemies(bestTile)) {
                bestTile = tile;
            }
        }
        return bestTile;
    }

    public int getNumberColoniesEnnemies(Tile tile) {
        int numberColonies = 0;
        for (TileVertex vertex : tile.getVertices()) {
            if (vertex.getBuilding() != null) {
                if (vertex.getBuilding().getOwner().getId() != getId()) {
                    numberColonies++;
                }
            }
        }
        return numberColonies;
    }

}
