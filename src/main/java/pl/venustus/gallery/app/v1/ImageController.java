package pl.venustus.gallery.app.v1;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Controller
public class ImageController {
	private final Path rootLocation;
	private final ImageService imageService;
	private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/";

	public ImageController(Path rootLocation, ImageService imageService) {
		this.rootLocation = rootLocation;
		this.imageService = imageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) {
		List<Image> images = imageService.loadAllImages(this.rootLocation);
		model.addAttribute("files1", images);

		return "gallery";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {

		Path file = this.rootLocation.resolve(filename);
		Resource resource = new UrlResource(file.toUri());

		return ResponseEntity
				.ok()
				.body(resource);
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename, RedirectAttributes redirectAttributes, Model model) throws Exception {
		if (file.getSize() == 0) {
			return REDIRECT_TO_MAIN_PAGE;
		}
		try {
			imageService.saveImage(this.rootLocation, file, filename);
			return REDIRECT_TO_MAIN_PAGE;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("innsertexception", e.getMessage());
			return REDIRECT_TO_MAIN_PAGE;
		}
	}

	@RequestMapping("/delete")
	public String findPhotos(@RequestParam("id") Long id) {
		imageService.deleteImage(id);

		return REDIRECT_TO_MAIN_PAGE;
	}
}
