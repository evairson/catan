package view.utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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
            System.out.println("Error" + e);
            return null;
        }
    }

    public static void updateImage(JLabel imageContainer, String filepath) {
        BufferedImage image;
        try {
            InputStream inputStream = ImgService.class.getResourceAsStream(filepath);
            image = ImageIO.read(inputStream);
            imageContainer.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }
}
