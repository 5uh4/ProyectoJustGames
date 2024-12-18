package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Videogame;
import utils.Listas;
import utils.Metodos;

public class VGDFavoritosController implements Initializable {

	@FXML
	private Button btnCerrarSesion;

	@FXML
	private Button btnVolver;

	@FXML
	private Button buscadorBtn;

	@FXML
	private TextField searchField;

	@FXML
	private Pagination pagination;

	@FXML
	private ListView<Videogame> listaFavoritos;

	private List<Integer> videojuegos;

	private static final int imagenPorPagina = 4;
	
	private static final int ITEMS_PER_PAGE = 4;

	
	@FXML
	void buscarFavoritos(ActionEvent event) {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// Comprobamos que la lista no esté vacía y que el usuario no sea incorrecto o
			// nulo
			if (Listas.userLogged == null || Listas.userLogged.getJuegosFavoritos() == null
					|| Listas.userLogged.getJuegosFavoritos().isEmpty()) {
				Metodos.mostrarAlerta("Lista vacía", "No has guardado ningún personaje como favorito");
			} else {
				// Si todo es correcto se carga la lista de usuarios y se muestra
				videojuegos = Listas.userLogged.getJuegosFavoritos();
				int pageCount = (int) Math.ceil((double) videojuegos.size() / imagenPorPagina);
				pagination.setPageCount(pageCount);
				pagination.setCurrentPageIndex(0);
				actualizarPagina(0);

				listaFavoritos.setCellFactory(param -> new ListCell<Videogame>() {
					@Override
					protected void updateItem(Videogame item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							VBox customRow = createCustomColumn(item);
							setGraphic(customRow);
						}
					}
				});

				pagination.setPageFactory(pageIndex -> {
					actualizarPagina(pageIndex);
					return new VBox(); // Solo para cumplir con el método, no se utiliza
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza la busqueda de los videojuegos a traves de la API
	 */
	@FXML
	private void buscarVideojuego() {
		String nombre = searchField.getText();
		if (nombre == null || nombre.trim().isEmpty()) {
			// Si el campo de búsqueda está vacío, restauramos la lista fija
			videojuegos = new ArrayList<>(videojuegos);
			int pageCount = (int) Math.ceil((double) videojuegos.size() / ITEMS_PER_PAGE);
			pagination.setPageCount(pageCount);
			pagination.setCurrentPageIndex(0);
			actualizarPagina(0);
		} else {
//			actualizarListaJuegos(nombre);
		}
	}

	@FXML
	void cerrarSesion(ActionEvent event) {
		Metodos.cierreDeSesion(event);
	}

	@FXML
	void mainScreen(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		Metodos.cambiarPantalla(stage, "/views/VGDMain.fxml");
	}

	/**
	 * Actualiza la pagina
	 * 
	 * @param pageIndex
	 */
	private void actualizarPagina(int pageIndex) {
		int fromIndex = pageIndex * imagenPorPagina;
		int toIndex = Math.min(fromIndex + imagenPorPagina, videojuegos.size());
	}

	/**
	 * Metodo robado de Jhon del Proyecto Pabliculas, modificado para crear columnas
	 * en lugar de filas
	 * 
	 * @author Jhon Deivis Ayala Braithwite
	 * @param personaje
	 * @return
	 */
	private VBox createCustomColumn(Videogame personaje) {
		VBox customRow = new VBox();

		// Configurar imagen principal
		ImageView imageView = new ImageView(new Image(personaje.getImage()));
		imageView.setFitHeight(300);
		imageView.setFitWidth(300);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);

		// Configurar texto
		Label pjNombreLabel = new Label(personaje.getTitle());
		pjNombreLabel.setAlignment(javafx.geometry.Pos.CENTER);
		pjNombreLabel.getStyleClass().add("titulo-label");

		Label estadoLabel = new Label(personaje.getDescription());
		Label especieLabel = new Label(String.valueOf(personaje.getPrecio()));

		Region space = new Region();
		HBox.setHgrow(space, javafx.scene.layout.Priority.ALWAYS);
		Region space2 = new Region();
		HBox.setHgrow(space2, javafx.scene.layout.Priority.ALWAYS);

		// Configurar diseno de la fila
		StackPane stackPane = new StackPane(imageView);
		customRow.getChildren().addAll(stackPane, space, pjNombreLabel, space2,
				createTextVBox(estadoLabel, especieLabel));
		customRow.setAlignment(javafx.geometry.Pos.CENTER);

		return customRow;
	}

	/**
	 * Crea el texto de la columna de la lista
	 * 
	 * @param estadoLabel
	 * @param especieLabel
	 * @return
	 */
	private VBox createTextVBox(Label estadoLabel, Label especieLabel) {
		VBox textVBox = new VBox(5); // Espaciado entre los elementos

		// Establecer estilos o clases de estilo si es necesario (puedes agregar estilos
		// en un archivo CSS)
		estadoLabel.getStyleClass().add("estado-label");
		especieLabel.getStyleClass().add("especie-label");
		Region space = new Region();
		space.setPrefHeight(10);

		textVBox.setAlignment(javafx.geometry.Pos.CENTER);

		// Agregar etiquetas al VBox
		textVBox.getChildren().addAll(estadoLabel, space, especieLabel);

		return textVBox;
	}

}
