import java.util.ArrayList;

public class Constants {

    public static class Game {
        public static final double SCALE = 1;
        public static int FPS_SET = 120;
        public static int UPS_SET = 200;
        public static int WIDTH = 0;
        public static int HEIGHT = 0;
    }

    public static class GameBoard {
        public static final int SIZE = 3;
    }

    public static class CubeCoordinatesConst {
        public static final ArrayList<CubeCoordinates> CubeDirections = new ArrayList<CubeCoordinates>() {
            {
                add(new CubeCoordinates(1, 0, -1));
                add(new CubeCoordinates(1, -1, 0));
                add(new CubeCoordinates(0, -1, 1));
                add(new CubeCoordinates(-1, 0, 1));
                add(new CubeCoordinates(-1, 1, 0));
                add(new CubeCoordinates(0, 1, -1));
            }
        };
    }

    public static class Tile {
        public static final int DESERT = 0;
        public static final int WOOD = 1;
        public static final int BRICK = 2;
        public static final int WHEAT = 3;
        public static final int SHEEP = 4;
        public static final int ORE = 5;

        public static final int QUANTITYOFWOOD = 4;
        public static final int QUANTITYOFBRICK = 3;
        public static final int QUANTITYOFWHEAT = 4;
        public static final int QUANTITYOFSHEEP = 4;
        public static final int QUANTITYOFORE = 3;
        public static final int QUANTITYOFDESERT = 1;
    }
}
