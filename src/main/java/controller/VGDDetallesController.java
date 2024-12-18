package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Videogame;
import utils.Listas;
import utils.Metodos;
import utils.DB.FDBC;

public class VGDDetallesController {

	private Videogame videojuegoActual;

	/**
	 * Imagen del videojuego
	 */
	@FXML
	private ImageView imageVG;

	/**
	 * Titulo del juego
	 */
	@FXML
	private Label titleLbl;

	/**
	 * Descripcion del videojuego
	 */
	@FXML
	private Label descriptionLbl;

	/**
	 * Especie a la que pertenece el personaje
	 */
	@FXML
	private Label precioLbl;

	@FXML
	private Button guardarBtn;

	/**
	 * Inicializa la pantalla con la informacion del videojuego
	 * 
	 * @param videojuego
	 */
	public void initData(Videogame videojuego) {
		this.videojuegoActual = videojuego;
		if (videojuego == null) {
			Metodos.mostrarAlerta("Error", "El videojuego seleccionado no es valido.");
			return;
		}
		// Aqui establecemos los datos del videojuego en los elementos de la interfaz
		if (titleLbl != null)
			titleLbl.setText(videojuego.getTitle());
		if (descriptionLbl != null)
			descriptionLbl.setText(videojuego.getDescription());
		if (precioLbl != null)
			precioLbl.setText(String.valueOf(videojuego.getPrecio()));
		// Cargamos la imagen
		if (imageVG != null) {
	        try {
	            Image image = new Image(videojuego.getImage(), true); // Carga en segundo plano
	            imageVG.setImage(image);
	            imageVG.setFitHeight(300);
	            imageVG.setFitWidth(300);
	            imageVG.setPreserveRatio(true);
	            imageVG.setSmooth(true);
	        } catch (Exception e) {
	            Metodos.mostrarAlerta("Error", "No se pudo cargar la imagen del videojuego.");
	            e.printStackTrace();
	        }
	    } else {
	        System.err.println("Error: El nodo 'imageVG' es nulo.");
	    }
	}

	@FXML
	void guardarEnFS(ActionEvent event) {
		// Hacer un metodo para checkear que el videojuego no este asociado al usuario
		if (!FDBC.obtenerVideojuego(videojuegoActual.getId()).equals(null)) {
			FDBC.crearVideojuego(videojuegoActual);
			Listas.userLogged.getJuegosFavoritos().add(videojuegoActual.getId());
			FDBC.actualizarUsuario(Listas.userLogged.getUsername(), Listas.userLogged);
			Metodos.mostrarAlerta("Guardado exitoso", "El juego se ha guardado en tu base de datos con exito");
		}
	}
}
