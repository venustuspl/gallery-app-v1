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

	public ImageController(Path rootLocation, ImageRepository imageRepository) {
		this.rootLocation = rootLocation;
		this.imageRepository = imageRepository;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws Exception {

		List<String> stringss = imageRepository.findAll().stream()
				.map(image -> this.rootLocation.resolve(image.getName()))
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(ImageController.class, "serveFile", path.getFileName().toString()).build()
						.toString())
				.collect(Collectors.toList());
		if (!stringss.isEmpty())
			System.out.println(stringss.get(0));
		model.addAttribute("files", stringss);

		List<Image> stringss1 = imageRepository.findAll().stream()
				.map(i -> new Image(i.getId(), MvcUriComponentsBuilder
						.fromMethodName(ImageController.class, "serveFile", this.rootLocation.resolve(i.getName())
								.getFileName().toString())
						.build()
						.toString()))
				.collect(Collectors.toList());
		if (!stringss1.isEmpty())
			System.out.println(stringss1.get(0).getName());
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
			return "redirect:/";
		}

		String imagePath = this.rootLocation.resolve(filename + ".jpg").toString();
		System.out.println(imagePath);
		List<Image> stringList = imageRepository.findAll();
		stringList.add(new Image(imagePath));
		Files.copy(file.getInputStream(), this.rootLocation.resolve(imagePath));

		imageRepository.save(new Image(imagePath));

		return "redirect:/";
	}

	@RequestMapping("/delete")
	public String findPhotos(@RequestParam("id") Long id) throws Exception {
//		System.out.println(text);
//		text = text.substring(text.lastIndexOf("/"));
//		text = this.rootLocation + text;
		System.out.println(id);
		imageRepository.deleteById(id);

		return "redirect:/";

	}
}
