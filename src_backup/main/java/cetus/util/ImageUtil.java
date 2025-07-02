package cetus.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageUtil {
    public static double minScale(File file, double size) throws IOException {

        BufferedImage img = ImageIO.read(file);

        int width = img.getWidth();
        int height = img.getHeight();

        int base = width > height ? height : width;

        if(base < size) {
            return 1.0D;
        }

        return size / base;
    }

    public static void saveToFile(BufferedImage thumbnail, String extension, File thumbnailFile) throws IOException {
        ImageIO.write(thumbnail, extension, thumbnailFile);
    }

}