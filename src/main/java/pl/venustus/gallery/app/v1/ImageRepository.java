package pl.venustus.gallery.app.v1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

	Image findByName(String text);

	Image findFirstByName(String text);

	Image deleteByName(String text);
}
