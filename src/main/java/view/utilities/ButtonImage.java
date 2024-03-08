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
    private Runnable action;

    public ButtonImage(String imagePath, String hoverImagePath, int xCoordBaseWidth,
                       int yCoordBaseHeight, double scale, Runnable action, MouseAdapter hoverEvent) {
        super();
        initializeButton(imagePath, hoverImagePath, xCoordBaseWidth, yCoordBaseHeight, scale,
                action, hoverEvent);
    }

    public void setAction(Runnable action) {
        this.addActionListener(e -> action.run());
    }

    private void initializeButton(String imagePath, String hoverImagePath, int xCoordBaseWidth,
                                  int yCoordBaseHeight, double scale, Runnable action,
                                  MouseAdapter hoverEvent) {
        try {
            // Coordonnées pour la résolution cible
            int[] coords = Resolution.calculateResolution(xCoordBaseWidth, yCoordBaseHeight);
            int xCoord = coords[0];
            int yCoord = coords[1];


            buttonImage = ImgService.loadImage(imagePath, scale);
            Image hoverOriginalImage = ImageIO.read(new File(hoverImagePath));
            int scaledWidth = buttonImage.getWidth(null);
            int scaledHeight = buttonImage.getHeight(null);
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
            addMouseListener(hoverEvent);

            addActionListener(e -> {
                if (action != null) {
                    action.run();
                }
            });

            setPreferredSize(new Dimension(scaledWidth, scaledHeight));
            setBorderPainted(false);
            setOpaque(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBounds(xCoord, yCoord, scaledWidth, scaledHeight);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
