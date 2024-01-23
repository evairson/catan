

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;




public class LoadSave {
    

    public static BufferedImage GetBufferedImage(String fileName){
        BufferedImage img = null;
        InputStream is = GetInputStream(fileName);
        try{
            img = ImageIO.read(is);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                is.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return img;
    }

    public static InputStream GetInputStream(String fileName){
        InputStream is = null;
        try{
            is = new FileInputStream(fileName);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return is;
    }
}