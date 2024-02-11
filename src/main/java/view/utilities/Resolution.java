package view.utilities;

import others.Constants;

public class Resolution {

    /***
     *
     * Convert x and y coordonnate based on 1280/720 screen
     * with coordonnates based on screen size of your computer.
     * @param xCoordBaseWidth
     * @param yCoordBaseHeight
     * @return array with two int, xCoord and yCoord
     */
    public static int[] calculateResolution(int xCoordBaseWidth, int yCoordBaseHeight) {

        // Résolution cible
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;

        // Calcul des facteurs d'échelle
        double scaleFactorX = (double) width / Constants.Game.BASE_WIDTH;
        double scaleFactorY = (double) height / Constants.Game.BASE_HEIGHT;

        // Coordonnées pour la résolution cible
        int xCoord = (int) (xCoordBaseWidth * scaleFactorX);
        int yCoord = (int) (yCoordBaseHeight * scaleFactorY);

        return new int[] {xCoord, yCoord};
    }

    public static double divider() {
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        double targetDiagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double diagonalRatio = targetDiagonal / Constants.Game.BASE_DIAGONAL;
        double divider = Constants.Game.BASE_DIVIDER_IMAGE / diagonalRatio;

        return divider;
    }

}
