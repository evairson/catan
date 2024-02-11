package view.utilities;

import others.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ButtonImage extends JButton {
    private Image buttonImage;
    private Image buttonHover;

    public ButtonImage(String imagePath, String hoverImagePath, int xCoordBaseWidth,
                       int yCoordBaseHeight, Runnable action) {
        super();
        initializeButton(imagePath, hoverImagePath, xCoordBaseWidth, yCoordBaseHeight, action);
    }

    private void initializeButton(String imagePath, String hoverImagePath, int xCoordBaseWidth,
                                  int yCoordBaseHeight, Runnable action) {
        try {
            // Résolution cible
            int width = Constants.Game.WIDTH;
            int height = Constants.Game.HEIGHT;

            // Calcul des facteurs d'échelle
            double scaleFactorX = (double) width / Constants.Game.BASE_WIDTH;
            double scaleFactorY = (double) height / Constants.Game.BASE_HEIGHT;

            // Coordonnées pour la résolution cible
            int xCoord = (int) (xCoordBaseWidth * scaleFactorX);
            int yCoord = (int) (yCoordBaseHeight * scaleFactorY);

            // Nouveau diviseur pour la résolution cible
            double targetDiagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
            double diagonalRatio = targetDiagonal / Constants.Game.BASE_DIAGONAL;
            double divider = Constants.Game.BASE_DIVIDER_IMAGE / diagonalRatio;

            // Charger l'image originale pour obtenir ses dimensions
            Image originalImage = ImageIO.read(new File(imagePath));
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);

            // Calculer les dimensions de l'image après division
            int scaledWidth = (int) (originalWidth / divider);
            int scaledHeight = (int) (originalHeight / divider);

            buttonImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            Image hoverOriginalImage = ImageIO.read(new File(hoverImagePath));
            buttonHover = hoverOriginalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            setIcon(new ImageIcon(buttonImage));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setIcon(new ImageIcon(buttonHover));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setIcon(new ImageIcon(buttonImage));
                }
            });

            addActionListener(e -> action.run());

            setPreferredSize(new Dimension(scaledWidth, scaledHeight));
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBounds(xCoord, yCoord, scaledWidth, scaledHeight);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
