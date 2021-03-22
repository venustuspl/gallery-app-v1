package pl.venustus.gallery.app.v1;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class ImageService {

    private final ThumbnailatorExample thumbnailatorExample;

    public ImageService(ThumbnailatorExample thumbnailatorExample) {
        this.thumbnailatorExample = thumbnailatorExample;
    }

    public void saveImageThumbnail(Path path, String fileLocation, String filename) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(fileLocation));
        BufferedImage outputImage = ThumbnailatorExample.resizeImage(originalImage, 200, 200);
        ImageIO.write(outputImage, "jpg", new File(path.toString() + "/" + filename + "_thumbnail.jpg"));
        System.out.println("Thumnail saved.");
    }
}
