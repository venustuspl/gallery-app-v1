package pl.venustus.gallery.app.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class GalleryApplication {
	public static void main(String[] args) {
		SpringApplication.run(GalleryApplication.class, args);
	}

	@Bean
	Path path() {
		return Paths.get(System.getProperty("java.io.tmpdir"));
	}
}
