package others;

import model.geometry.Orientation;
import model.geometry.CubeCoordinates;
import view.TileType;
import view.utilities.Resolution;

import java.awt.*;
import java.util.HashMap;

public class Constants {
    public static class CubeCoordinatesConst {
        public static final CubeCoordinates[] DIRECTIONS = new CubeCoordinates[] {
            new CubeCoordinates(1, 0, -1), new CubeCoordinates(1, -1, 0),
            new CubeCoordinates(0, -1, 1), new CubeCoordinates(-1, 0, 1),
            new CubeCoordinates(-1, 1, 0), new CubeCoordinates(0, 1, -1)
        };
    }

    public static class BuildingCosts {
        public static final int[] ROAD = new int[] {1, 0, 0, 1, 0};
        public static final int[] COLONY = new int[] {1, 0, 1, 1, 1};
        public static final int[] CITY = new int[] {0, 3, 2, 0, 0};

        public static boolean canBuildRoad(HashMap<TileType, Integer> resources) {
            for (int i = 0; i < resources.size(); i++) {
                if (resources.get(TileType.WOOD) < ROAD[3]) {
                    return false;
                }
                if (resources.get(TileType.CLAY) < ROAD[0]) {
                    return false;
                }
            }
            return true;
        }

        public static boolean canBuildColony(HashMap<TileType, Integer> resources) {
            if (resources.get(TileType.WOOD) < COLONY[3]) {
                return false;
            }
            if (resources.get(TileType.CLAY) < COLONY[0]) {
                return false;
            }
            if (resources.get(TileType.WHEAT) < COLONY[2]) {
                return false;
            }
            if (resources.get(TileType.WOOL) < COLONY[4]) {
                return false;
            }
            return true;
        }

        public static boolean canBuildCity(HashMap<TileType, Integer> resources) {
            for (int i = 0; i < resources.size(); i++) {
                if (resources.get(TileType.WHEAT) < CITY[2]) {
                    return false;
                }
                if (resources.get(TileType.ORE) < CITY[1]) {
                    return false;
                }
            }
            return true;
        }

    }

    public static class Game {
        public static final double SCALE = 1;
        public static final int FPS_SET = 120;
        public static final int UPS_SET = 200;

        public static final int BASE_WIDTH = 1280;
        public static final int BASE_HEIGHT = 720;
        public static final double BASE_DIAGONAL = Math.sqrt(Math.pow(BASE_WIDTH, 2)
                + Math.pow(BASE_HEIGHT, 2));
        public static final int BASE_DIVIDER_IMAGE = 2;



        private static final Dimension[] TEST_SCREEN_SIZES = {
            new Dimension(1024, 768),    // 4:3         0
            new Dimension(1280, 800),    // 16:10       1
            new Dimension(1366, 768),    // ~16:9       2
            new Dimension(1440, 900),    // 16:10       3
            new Dimension(1600, 900),    // 16:9        4
            new Dimension(1920, 1080),   // 16:9        5
        };
        private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
        public static final Dimension ADJUSTED_SIZE = adjustToAspectRatio(checkIfWindows(SCREEN_SIZE));
        public static final int WIDTH = ADJUSTED_SIZE.width;
        public static final int HEIGHT = ADJUSTED_SIZE.height;

        public static final double DIVIDER = Resolution.divider();

        private static Dimension checkIfWindows(Dimension screenSize) {
            String osName = System.getProperty("os.name").toLowerCase();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();

            if (osName.contains("win")) {
                Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getMaximumWindowBounds();
                height = winSize.getHeight();
            }
            return new Dimension((int) width, (int) height);
        }

        private static Dimension adjustToAspectRatio(Dimension screenSize) {
            // Ratio cible 16:9
            double targetRatio = 16.0 / 9.0;
            // Ratio actuel de l'écran
            double currentRatio = (double) screenSize.width / screenSize.height;

            int width;
            int height;

            if (currentRatio > targetRatio) {
                width = (int) (screenSize.height * targetRatio);
                height = screenSize.height;
            } else if (currentRatio < targetRatio) {
                width = screenSize.width;
                height = (int) (screenSize.width / targetRatio);
            } else {
                return screenSize;
            }

            // Assurer que la nouvelle dimension est inférieure ou égale à la taille de l'écran actuel
            width = Math.min(width, screenSize.width);
            height = Math.min(height, screenSize.height);

            return new Dimension(width, height);
        }
    }

    public static class BoardConstants {
        public static final int[] TILE_DICE_VALUES_DEFAULT = new int[] {9, 8, 5, 12, 11, 3, 6, 10, 6, 4, 11,
                                                                        2, 4, 3, 5, 9, 10, 8, 0};

        public static final int DESERT = 0;
        public static final int WOOD = 1;
        public static final int WHEAT = 2;
        public static final int CLAY = 3;
        public static final int WOOL = 4;
        public static final int ORE = 5;

        public static final int[] TILE_TYPES_DEFAULT = new int[] {WOOD, WOOD, WOOD, WOOD, WHEAT, WHEAT,
            WHEAT, WHEAT, CLAY, CLAY, CLAY, WOOL, WOOL, WOOL, WOOL, ORE, ORE, ORE};

        private static int[] randomizeArray(int[] arr) {
            for (int i = arr.length - 1; i > 0; i--) {
                int index = (int) (Math.random() * (arr.length));
                if (arr[index] == 6 || arr[index] == 8 || arr[i] == 6 || arr[i] == 8) {
                    continue; // skip the 8 and 6 dice values
                }
                int a = arr[index];
                arr[index] = arr[i];
                arr[i] = a;
            }
            return arr;
        }

        public static final int[] TILE_DICE_VALUES = randomizeArray(TILE_DICE_VALUES_DEFAULT);
        public static final int[] TILE_TYPES = randomizeArray(TILE_TYPES_DEFAULT);
    }

    public static class Number {
        public static final int SECOND = 1000;
        public static final double DOUBLE_BILLION = 1000000000.0;

        // variables racistes de test
        public static final int HEXAGON_SIDES = 6;
        public static final int TEN = 10;
        public static final int TWENTY = 20;
    }

    public static class OrientationConstants {
        public static final Orientation FLAT = new Orientation(0.0, 3.0 / 2.0, 0.0,
                Math.sqrt(3.0) / 2.0, Math.sqrt(3.0), 2.0 / 3.0, 0.0,
                -1.0 / 3.0, Math.sqrt(3.0) / 3.0);

        public static final Orientation POINTY = new Orientation(0.5, Math.sqrt(3.0),
                Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0,
                -1.0 / 3.0, 0.0, 2.0 / 3.0);

    }

    public static class Others {
        public static final String MUSIC_DIRECTORY = "src/main/resources/music";
    }
}
