package others;

import model.geometry.Orientation;

public class Constants {
    public static class CubeCoordinatesConst {
        public static final Orientation[] DIRECTIONS = new Orientation[] {
                new Orientation(1, 0, -1), new Orientation(1, -1, 0),
                new Orientation(0, -1, 1), new Orientation(-1, 0, 1),
                new Orientation(-1, 1, 0), new Orientation(0, 1, -1)
        };
    }

    public static class Game {
        public static final double SCALE = 1;
        public static final int FPS_SET = 120;
        public static final int UPS_SET = 200;
        public static final int WIDTH = 0;
        public static final int HEIGHT = 0;
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
