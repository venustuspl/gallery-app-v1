package pl.venustus.gallery.app.v1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

	Long findByName(String text);

	Image findFirstByName(String text);

	void deleteByName(String text);
}
