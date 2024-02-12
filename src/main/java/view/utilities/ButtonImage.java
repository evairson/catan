package view.utilities;

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
                       int yCoordBaseHeight, double scale, Runnable action) {
        super();
        initializeButton(imagePath, hoverImagePath, xCoordBaseWidth, yCoordBaseHeight, scale, action);
    }

    private void initializeButton(String imagePath, String hoverImagePath, int xCoordBaseWidth,
                                  int yCoordBaseHeight, double scale, Runnable action) {
        try {

            // Coordonnées pour la résolution cible
            int xCoord = Resolution.calculateResolution(xCoordBaseWidth, yCoordBaseHeight)[0];
            int yCoord = Resolution.calculateResolution(xCoordBaseWidth, yCoordBaseHeight)[1];

            // Nouveau diviseur pour la résolution cible
            double divider = scale * Resolution.divider();

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
