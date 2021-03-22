package pl.venustus.gallery.app.v1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String url;

	private String thumbnailUrl;

	public Image() {
	}

	public Image(String name, String url, String thumbnailUrl) {
		this.name = name;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
	}

	public Image(Long id, String name, String url, String thumbnailUrl) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@Override
	public String toString() {
		return "Image{" +
				"id=" + id +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				", thumbnailUrl='" + thumbnailUrl + '\'' +
				'}';
	}
}
