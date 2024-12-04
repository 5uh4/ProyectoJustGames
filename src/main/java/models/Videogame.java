package models;

import java.util.List;

/**
 * Rough model for storage under the Firestore DB
 * @author thesu
 *
 */
public class Videogame {

	private int id;
	private String title;
	private String description;
	private List<String> plataformas;
	private double precio;
	private List<String> tiendas;

	public Videogame() {
		super();
	}

	public Videogame(int id, String title, String description, List<String> plataformas, double precio,
			List<String> tiendas) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.plataformas = plataformas;
		this.precio = precio;
		this.tiendas = tiendas;
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

	public List<String> getPlataformas() {
		return plataformas;
	}

	public void setPlataformas(List<String> plataformas) {
		this.plataformas = plataformas;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public List<String> getTiendas() {
		return tiendas;
	}

	public void setTiendas(List<String> tiendas) {
		this.tiendas = tiendas;
	}

	@Override
	public String toString() {
		return "Videojuegos [id=" + id + ", title=" + title + ", description=" + description + ", plataformas="
				+ plataformas + ", precio=" + precio + ", tiendas=" + tiendas + "]";
	}

	public String toJsonString() {
		return "Videojuegos {\n\t" + "\"id\": \"" + id + "\",\n\t\"title\": \"" + title + "\",\n\t\"description\": \"" + description
				+ "\",\n\t\"plataformas\\\": \"" + plataformas + "\",\n\t \"precio\": \"" + precio + "\",\n\t \"tiendas\": \"" + tiendas
				+ "\"\n}";
	}

}
