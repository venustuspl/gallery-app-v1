package pl.venustus.gallery.app.v1;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ImageController {
	private final ImageRepository imageRepository;
	private final Path rootLocation;
	private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/";

	public ImageController(Path rootLocation, ImageRepository imageRepository) {
		this.rootLocation = rootLocation;
		this.imageRepository = imageRepository;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws Exception {
		List<Image> stringss1 = imageRepository.findAll().stream()
				.map(i -> new Image(i.getId(), i.getName(), MvcUriComponentsBuilder
						.fromMethodName(ImageController.class, "serveFile", this.rootLocation.resolve(i.getUrl())
								.getFileName().toString())
						.build()
						.toString()))
				.collect(Collectors.toList());
		model.addAttribute("files1", stringss1);

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
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename, RedirectAttributes redirectAttributes
	) throws Exception {

		if (file.getSize() == 0) {
			return REDIRECT_TO_MAIN_PAGE;
		}

		String imagePath = this.rootLocation.resolve(filename + ".jpg").toString();
		System.out.println(imagePath);
		List<Image> stringList = imageRepository.findAll();
		stringList.add(new Image(filename, imagePath));
		Files.copy(file.getInputStream(), this.rootLocation.resolve(imagePath));

		imageRepository.save(new Image(filename, imagePath));

		return REDIRECT_TO_MAIN_PAGE;
	}

	@RequestMapping("/delete")
	public String findPhotos(@RequestParam("id") Long id) throws Exception {
		imageRepository.deleteById(id);

		return REDIRECT_TO_MAIN_PAGE;
	}
}
