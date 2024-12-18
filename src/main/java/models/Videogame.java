package models;

public class Videogame {

	private int id;
	private String title;
	private String description;
	private double precio;
	private String image;

	public Videogame() {
	}

	public Videogame(int id, String title, String description, double precio, String image) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.precio = precio;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Videogame [id=" + id + ", title=" + title + ", description=" + description + ", precio=" + precio
				+ ", image=" + image + "]";
	}

}
