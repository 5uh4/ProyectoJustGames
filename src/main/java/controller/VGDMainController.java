package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Videogame;
import utils.Listas;
import utils.Metodos;
import utils.StoreData.RealSteamAPI;

public class VGDMainController implements Initializable {

	@FXML
	private ListView<Videogame> listaJuegosUsuario;
	
	@FXML
	private ListView<Videogame> listaVideojuegosFX;

	@FXML
	private TextField searchField;

	@FXML
	private Button searchButton;

	@FXML
	private Button btnLoggear;

	@FXML
	private Pagination pagination;

	private List<Videogame> videojuegosFijos = new ArrayList<>();
	private List<Videogame> videojuegosTemporales = new ArrayList<>();
	private List<Videogame> listaJuegosActiva = new ArrayList<>();

	private static final int ITEMS_PER_PAGE = 4;

	@FXML
	private Button btnFavoritos;

	/**
	 * Inicializa la pantalla con la lista de Videojuegos
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if (listaVideojuegosFX == null) {
		    System.out.println("Error: listaVideojuegosFX no esta inicializada");
		    return;
		}
		try {
			videojuegosFijos.add(RealSteamAPI.fetchSteamGameDetails("292030")); // The Witcher
			videojuegosFijos.add(RealSteamAPI.fetchSteamGameDetails("1245620")); // Elden Ring
			videojuegosFijos.addAll(RealSteamAPI.searchGamesByName("Cyberpunk 2077"));

			listaJuegosActiva = videojuegosFijos;
			
			int pageCount = (int) Math.ceil((double) listaJuegosActiva.size() / ITEMS_PER_PAGE);
			pagination.setPageCount(pageCount);
			pagination.setCurrentPageIndex(0);
			actualizarPagina(0);

			listaVideojuegosFX.setCellFactory(param -> new ListCell<Videogame>() {
				@Override
				protected void updateItem(Videogame item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
						setStyle("-fx-background-color: transparent;");
					} else {
						VBox customRow = createCustomRow(item);
						customRow.setPrefSize(300, 350);
						setGraphic(customRow);
					}
				}
			});

			listaVideojuegosFX.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						Videogame selectedGame = listaVideojuegosFX.getSelectionModel().getSelectedItem();
						if (selectedGame != null) {
							abrirDetallesVideojuego(selectedGame);
						}
					}
				}
			});

			pagination.setPageFactory(pageIndex -> {
				actualizarPagina(pageIndex);
				return new VBox(); // Solo para cumplir con el metodo, no se utiliza
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void mostrarFavoritos(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		if (Listas.userLogged != null) {
			Metodos.cambiarPantalla(stage, "/views/VGDFavoritos.fxml");
		} else {
			Metodos.mostrarAlerta("No logueado", "Por favor, inicia sesión para ver favoritos.");
			Metodos.cambiarPantalla(stage, "/views/VGDLogin.fxml");
		}
	}

	@FXML
	private void doLogin(ActionEvent event) {
		if (Listas.userLogged != null) {
			Metodos.cierreDeSesion(event);
		} else {
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			Metodos.cambiarPantalla(stage, "/views/VGDLogin.fxml");
		}
	}


	private void abrirDetallesVideojuego(Videogame videojuego) {
		if (videojuego == null) {
			// Manejar el caso en el que el personaje es nulo si es necesario
			Metodos.mostrarAlerta("Error", "No se ha seleccionado un videojuego valido.");
			return;
		}

		try {
			// Cargar el archivo FXML de la vista de detalles del personaje
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VGDDetalles.fxml"));
			Parent root = loader.load();

			 // Usa Platform.runLater para asegurar la inicialización correcta de nodos
	        Platform.runLater(() -> {
	            // Obtener el controlador de la vista de detalles
	            VGDDetallesController videogameController = loader.getController();

	            // Pasar los datos del videojuego seleccionado
	            videogameController.initData(videojuego);

	            // Mostrar la nueva ventana
	            Scene scene = new Scene(root);
	            Stage stage = new Stage();
	            stage.setTitle(videojuego.getTitle());
	            stage.getIcons().add(new Image("file:src/main/resources/JustGamesFaviconNoBG.png"));
	            stage.setScene(scene);
	            stage.show();
	        });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/* 
 * PARA IMPLEMENTAR UNA FORMA VISUAL DE INDICAR QUE ESTA CARGANDO
 * 
 * FXML
private void buscarVideojuego() {
    String nombre = searchField.getText();
    
    // Mostrar el indicador de carga
    progressIndicator.setVisible(true);
    loadingLabel.setVisible(true);

    // Ejecutar la búsqueda en un hilo separado para no bloquear la interfaz gráfica
    new Thread(() -> {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                // Si el campo de búsqueda está vacío, restauramos la lista fija
                listaJuegosActiva = new ArrayList<>(videojuegosFijos);
            } else {
                listaJuegosActiva = RealSteamAPI.searchGamesByName(nombre);
            }

            // Actualizar la interfaz gráfica desde el hilo principal
            Platform.runLater(() -> {
                if (listaJuegosActiva.isEmpty()) {
                    Metodos.mostrarAlerta("Videojuego no encontrado", "No se encontraron Videojuegos con el nombre: " + nombre);
                    listaJuegosActiva = new ArrayList<>(videojuegosFijos);
                }

                int pageCount = (int) Math.ceil((double) listaJuegosActiva.size() / ITEMS_PER_PAGE);
                pagination.setPageCount(pageCount);
                pagination.setCurrentPageIndex(0);
                actualizarPagina(0);

                // Ocultar el indicador de carga
                progressIndicator.setVisible(false);
                loadingLabel.setVisible(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                // Ocultar el indicador y mostrar error
                progressIndicator.setVisible(false);
                loadingLabel.setVisible(false);
                Metodos.mostrarAlerta("Error", "Ocurrió un problema durante la búsqueda.");
            });
        }
    }).start();
}

 * 
 * 
 * 
 * @Override
public void initialize(URL location, ResourceBundle resources) {
    progressIndicator.setVisible(true);
    loadingLabel.setVisible(true);

    new Thread(() -> {
        try {
            videojuegosFijos.add(RealSteamAPI.fetchSteamGameDetails("292030")); // The Witcher
            videojuegosFijos.add(RealSteamAPI.fetchSteamGameDetails("1245620")); // Elden Ring
            videojuegosFijos.addAll(RealSteamAPI.searchGamesByName("Cyberpunk 2077"));

            listaJuegosActiva = videojuegosFijos;

            Platform.runLater(() -> {
                int pageCount = (int) Math.ceil((double) listaJuegosActiva.size() / ITEMS_PER_PAGE);
                pagination.setPageCount(pageCount);
                pagination.setCurrentPageIndex(0);
                actualizarPagina(0);

                progressIndicator.setVisible(false);
                loadingLabel.setVisible(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                loadingLabel.setVisible(false);
                Metodos.mostrarAlerta("Error", "No se pudieron cargar los videojuegos.");
            });
        }
    }).start();
}**/
	/**
	 * Realiza la busqueda de los videojuegos a traves de la API
	 */
	@FXML
	private void buscarVideojuego() {
		String nombre = searchField.getText();
		if (nombre == null || nombre.trim().isEmpty()) {
		       // Si el campo de búsqueda está vacío, restauramos la lista fija
	        listaJuegosActiva = new ArrayList<>(videojuegosFijos);
	        int pageCount = (int) Math.ceil((double) listaJuegosActiva.size() / ITEMS_PER_PAGE);
	        pagination.setPageCount(pageCount);
	        pagination.setCurrentPageIndex(0);
	        actualizarPagina(0);
		} else {
			actualizarListaJuegos(nombre);
		}
	}

	/**
	 * Actualiza la lista de Videojuegos
	 * 
	 * @param nombre Nombre del Videojuego que queremos buscar
	 */

	private void actualizarListaJuegos(String nombre) {
		listaJuegosActiva = RealSteamAPI.searchGamesByName(nombre);
		if (listaJuegosActiva.isEmpty()) {
			Metodos.mostrarAlerta("Videojuego no encontrado", "No se encontraron Videojuegos con el nombre: " + nombre);
		      // Restauramos la lista fija si no hay resultados
	        listaJuegosActiva = new ArrayList<>(videojuegosFijos);
	        actualizarPagina(0);
		} else {
			int pageCount = (int) Math.ceil((double) listaJuegosActiva.size() / ITEMS_PER_PAGE);
			pagination.setPageCount(pageCount);
			pagination.setCurrentPageIndex(0);
			actualizarPagina(0);
		}
	}

	/**
	 * Actualiza la paginacion
	 * 
	 * @param pageIndex
	 */
	private void actualizarPagina(int pageIndex) {
		int fromIndex = pageIndex * ITEMS_PER_PAGE;
		int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, listaJuegosActiva.size());
		listaVideojuegosFX.getItems().setAll(listaJuegosActiva.subList(fromIndex, toIndex));
	}

	private VBox createCustomRow(Videogame Videojuego) {
		VBox customRow = new VBox();

		// Configurar imagen principal
		ImageView imageView = new ImageView(new Image(Videojuego.getImage()));
		imageView.setFitHeight(300);
		imageView.setFitWidth(300);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);

		// Configurar texto
		Label pjNombreLabel = new Label(Videojuego.getTitle());
		pjNombreLabel.setAlignment(javafx.geometry.Pos.CENTER);
		pjNombreLabel.getStyleClass().add("titulo-label");

		Label estadoLabel = new Label(String.valueOf(Videojuego.getPrecio()));

		Region space = new Region();
		HBox.setHgrow(space, javafx.scene.layout.Priority.ALWAYS);
		Region space2 = new Region();
		HBox.setHgrow(space2, javafx.scene.layout.Priority.ALWAYS);

		// Configurar diseÃ±o de la fila
		StackPane stackPane = new StackPane(imageView);
		customRow.getChildren().addAll(stackPane, space, pjNombreLabel, space2, estadoLabel);
		customRow.setAlignment(javafx.geometry.Pos.CENTER);

		return customRow;
	}

}
