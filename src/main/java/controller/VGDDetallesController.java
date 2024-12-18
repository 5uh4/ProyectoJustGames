package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
	private TextArea descriptionLbl;

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
		if (Listas.userLogged == null) {
	        Metodos.mostrarAlerta("Error", "Debes iniciar sesión para guardar videojuegos.");
	        return;
	    }

	    Task<Void> guardarTarea = new Task<>() {
	        @Override
	        protected Void call() {
	            try {
	                // Verificar si el videojuego ya existe en la base de datos
	                Videogame videojuegoEnBD = FDBC.obtenerVideojuego(videojuegoActual.getId());
	                if (videojuegoEnBD == null) {
	                    // Si el videojuego no existe en la base de datos, guardarlo
	                    FDBC.crearVideojuego(videojuegoActual);
	                }

	                // Comprobar si el juego ya está en la lista de favoritos del usuario
	                if (!Listas.userLogged.getJuegosFavoritos().contains(videojuegoActual.getId())) {
	                    // Añadir el juego a la lista de favoritos del usuario
	                    Listas.userLogged.getJuegosFavoritos().add(videojuegoActual.getId());
	                    FDBC.actualizarUsuario(Listas.userLogged.getUsername(), Listas.userLogged);

	                    // Notificar al usuario del éxito
	                    Platform.runLater(() -> Metodos.mostrarAlerta("Guardado exitoso", "El juego se ha guardado correctamente en tu lista de favoritos."));
	                } else {
	                    // Notificar si el juego ya está en la lista
	                    Platform.runLater(() -> Metodos.mostrarAlerta("Información", "El juego ya está en tu lista de favoritos."));
	                }
	            } catch (Exception e) {
	                // Manejar errores y notificar al usuario
	                Platform.runLater(() -> Metodos.mostrarAlerta("Error", "No se pudo guardar el videojuego. Por favor, inténtalo de nuevo."));
	                e.printStackTrace();
	            }
	            return null;
	        }
	    };

	    // Ejecutar la tarea en un hilo separado
	    new Thread(guardarTarea).start();
	}
}
