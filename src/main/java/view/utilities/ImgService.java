package view.utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImgService {

    public static JLabel loadImage(String filepath) {

        BufferedImage image;
        JLabel imageContainer;

        try {
            InputStream stream = ImgService.class.getResourceAsStream(filepath);
            image = ImageIO.read(stream);
            imageContainer = new JLabel(new ImageIcon(image));
            return imageContainer;
        } catch (Exception e) {
            return null;
        }
    }

    public static Image loadImage(String imagePath, double scale) throws IOException {

        // Nouveau diviseur pour la résolution cible
        double divider = scale * Resolution.divider();

        // Charger l'image originale pour obtenir ses dimensions
        Image originalImage = ImageIO.read(new File(imagePath));
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // Calculer les dimensions de l'image après division
        int scaledWidth = (int) (originalWidth / divider);
        int scaledHeight = (int) (originalHeight / divider);

        return originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }

    public static void updateImage(JLabel label, String imagePath, double scale) {
        try {
            // Charger l'image originale depuis le chemin fourni
            URL imageUrl = ImgService.class.getResource(imagePath);
            Image originalImage = ImageIO.read(imageUrl);

            // Obtenir le facteur de division pour le redimensionnement
            double divider = scale * Resolution.divider();

            // Calculer les nouvelles dimensions de l'image
            int scaledWidth = (int) (originalImage.getWidth(null) / divider);
            int scaledHeight = (int) (originalImage.getHeight(null) / divider);

            // Redimensionner l'image
            Image resizedImage = originalImage.getScaledInstance(scaledWidth, scaledHeight,
                    Image.SCALE_SMOOTH);

            // Mettre à jour le JLabel avec la nouvelle image redimensionnée
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            label.setIcon(resizedIcon);

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + imagePath);
            e.printStackTrace();
        }
    }
}
