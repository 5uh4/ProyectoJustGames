package models;

import java.util.List;

/**
 * Modelo para almacenar los usuarios en la BD de Firestore
 * @author thesu
 *
 */
public class Users {
	private String username;
	private String password;
	private List<Integer> juegosFavoritos;

	public Users() {
		super();
	}

	public Users(String username, String password, List<Integer> juegosFavoritos) {
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

	public List<Integer> getJuegosFavoritos() {
		return juegosFavoritos;
	}

	public void setJuegosFavoritos(List<Integer> juegosFavoritos) {
		this.juegosFavoritos = juegosFavoritos;
	}

	@Override
	public String toString() {
		return "Users [username=" + username + ", password=" + password + ", juegosFavoritos=" + juegosFavoritos + "]";
	}

	
}
