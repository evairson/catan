package others;

import model.geometry.Orientation;
import model.geometry.CubeCoordinates;

import java.awt.*;


public class Constants {
    public static class CubeCoordinatesConst {
        public static final CubeCoordinates[] DIRECTIONS = new CubeCoordinates[] {
            new CubeCoordinates(1, 0, -1), new CubeCoordinates(1, -1, 0),
            new CubeCoordinates(0, -1, 1), new CubeCoordinates(-1, 0, 1),
            new CubeCoordinates(-1, 1, 0), new CubeCoordinates(0, 1, -1)
        };
    }

    public static class Game {
        public static final double SCALE = 1;
        public static final int FPS_SET = 120;
        public static final int UPS_SET = 200;

        public static final int BASE_WIDTH = 1280;
        public static final int BASE_HEIGHT = 720;
        public static final double BASE_DIAGONAL = 1468.6047; // Th. de Pytaghore
        public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
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
        public static final String MUSIC_DIRECTORY = "";
    }

}
