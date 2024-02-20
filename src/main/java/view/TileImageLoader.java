package view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class TileImageLoader {

    public static Map<TileType, BufferedImage> loadAndResizeTileImages() {
        Map<TileType, BufferedImage> tileImages = new EnumMap<>(TileType.class);
        // Charger et redimensionner chaque image pour chaque type de tuile
        for (TileType type : TileType.values()) {
            BufferedImage originalImage = loadImage(type.getImagePath());
            if (originalImage != null) {
                BufferedImage resizedImage = resizeImage(originalImage, originalImage.getWidth() / 2,
                        originalImage.getHeight() / 2);
                tileImages.put(type, resizedImage);
            }
        }
        return tileImages;
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + path);
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resized.getGraphics().drawImage(tmp, 0, 0, null);
        return resized;
    }
}
