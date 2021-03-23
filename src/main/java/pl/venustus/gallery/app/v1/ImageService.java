package pl.venustus.gallery.app.v1;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public static void saveImageThumbnail(Path path, String fileLocation, String filename) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(fileLocation));
        BufferedImage outputImage = Thumbnailator.resizeImage(originalImage, 200, 200);
        ImageIO.write(outputImage, "jpg", new File(path.toString() + "/" + filename + "_thumbnail.jpg"));
    }

    public List<Image> loadAllImages(Path rootLocation) {
        return imageRepository.findAll().stream()
                .map(i -> new Image(i.getId(), i.getName(), MvcUriComponentsBuilder
                                .fromMethodName(ImageController.class, "serveFile", rootLocation.resolve(i.getUrl())
                                        .getFileName().toString())
                                .build()
                                .toString(),
                                MvcUriComponentsBuilder
                                        .fromMethodName(ImageController.class, "serveFile", rootLocation.resolve(i.getThumbnailUrl())
                                                .getFileName().toString())
                                        .build()
                                        .toString()
                        )
                )
                .collect(Collectors.toList());
    }

    public void saveImage(Path rootLocation, MultipartFile file, String filename) throws Exception {
        String imagePath = rootLocation.resolve(filename + ".jpg").toString();
        String imageThumbnailPath = rootLocation.resolve(filename + "_thumbnail.jpg").toString();

        try {
            Files.copy(file.getInputStream(), rootLocation.resolve(imagePath));
        } catch (Exception e) {
            throw new Exception("Another file exists with the same name!");
        }

        saveImageThumbnail(rootLocation, imagePath, filename);
        imageRepository.save(new Image(filename, imagePath, imageThumbnailPath));
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
