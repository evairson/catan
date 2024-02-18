package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class TileImageLoader {

    public static Map<TileType, BufferedImage> loadTileImages() {
        Map<TileType, BufferedImage> tileImages = new HashMap<>();
        for (TileType type : TileType.values()) {
            try {
                BufferedImage img = ImageIO.read(new File(type.getImagePath()));
                tileImages.put(type, img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tileImages;
    }




}
