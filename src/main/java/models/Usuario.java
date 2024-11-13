package models;

import java.util.List;

/**
 * Modelo para almacenar los usuarios en la BD de Firestore
 * @author thesu
 *
 */
public class Usuario {
	private String username;
	private String password;
	private List<Videojuegos> juegosFavoritos;

	public Usuario() {
		super();
	}

	public Usuario(String username, String password, List<Videojuegos> juegosFavoritos) {
		super();
		this.username = username;
		this.password = password;
		this.juegosFavoritos = juegosFavoritos;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Videojuegos> getJuegosFavoritos() {
		return juegosFavoritos;
	}

	public void setJuegosFavoritos(List<Videojuegos> juegosFavoritos) {
		this.juegosFavoritos = juegosFavoritos;
	}

	@Override
	public String toString() {
		return "Usuario [username=" + username + ", password=" + password + ", juegosFavoritos=" + juegosFavoritos
				+ "]";
	}
}
