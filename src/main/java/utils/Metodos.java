package utils;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Metodos {
	/**
	 * Metodo general para mostrar alertas necesarias 
	 * @param titulo Titulo de la pantalla de alerta
	 * @param mensaje Cuerpo del mensaje de alerta
	 */
	public static void mostrarAlerta(String titulo, String mensaje) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

	/**
	 * Metodo que permite cambiar de pantalla. Se mantiene aqu� para reducir la
	 * cantidad de loneas de codigo en otras pantallas
	 * 
	 * @param currentStage Pantalla actual, necesario para que se cierre
	 * @param fxmlPath     Localizacion del archivo FXML para poder abrirlo
	 */
	public static void cambiarPantalla(Stage currentStage, String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
			Parent root = loader.load();
			Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.setTitle("Just Games");
			newStage.getIcons().add(new Image("file:src/main/java/resources/JustGamesFaviconNoBG.png"));

			newStage.show();
			currentStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que permite cambiar de pantalla. Se mantiene aqu� para reducir la
	 * cantidad de loneas de codigo en otras pantallas
	 * 
	 * @param currentStage Pantalla actual, necesario para que se cierre
	 * @param fxmlPath     Localizacion del archivo FXML para poder abrirlo
	 * @param title        Titulo que queramos ponerle a la pantalla
	 * @param iconPath     Localizacion de la imagen de icono
	 */
	public static void mostrarLogin(Stage currentStage, String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
			Parent root = loader.load();
			Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.setTitle("Just Games");
			newStage.getIcons().add(new Image("file:src/main/java/resources/JustGamesFaviconNoBG.png"));
			
			newStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Metodo para mostrar una nueva pantalla sin cerrar la actual.
     * Ideal para ventanas modales como "Detalles".
     * @param fxmlPath Localizacion del archivo FXML para abrir la nueva pantalla
     */
    public static void mostrarPantallaModal(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(root));
            modalStage.setTitle("Detalles del Videojuego");
            modalStage.getIcons().add(new Image("file:src/main/java/resources/JustGamesFaviconNoBG.png"));

            // Establecer la ventana como modal
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setResizable(false);

            modalStage.showAndWait(); // Espera a que el usuario cierre esta ventana antes de continuar
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo para mostrar pantallas superpuestas (sin cerrar la actual).
     *
     * @param fxmlPath Ruta del archivo FXML.
     * @param onLoad Callback opcional para inicializar datos en el controlador.
     */
    public static void mostrarSuperpuesta(String fxmlPath, Consumer<Object> onLoad) {
        try {
            FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
            Parent root = loader.load();

            if (onLoad != null) {
                Object controller = loader.getController();
                onLoad.accept(controller);
            }

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Just Games");
            newStage.getIcons().add(new Image("file:src/main/java/resources/JustGamesFaviconNoBG.png"));
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana solicitada.");
        }
    }

    
    /**
     * Metodo para recargar la pantalla principal, como en casos de login.
     * @param currentStage Pantalla actual
     * @param fxmlPath Localizacion del archivo FXML para recargar la pantalla
     */
    public static void recargarPantalla(Stage currentStage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Actualiza la escena en lugar de abrir una nueva ventana
            currentStage.setScene(scene);
            currentStage.setTitle("Just Games");
            currentStage.getIcons().add(new Image("file:src/main/java/resources/JustGamesFaviconNoBG.png"));

            currentStage.show(); // Muestra la ventana actualizada
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	

    /**
     * Metodo de cierre de sesion.
     * Se asegura de recargar la pantalla principal para reflejar cambios.
     */
    public static void cierreDeSesion(ActionEvent event) {
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Listas.userLogged = null;
        recargarPantalla(currentStage, "/views/VGDMain.fxml");
        mostrarAlerta("Sesión cerrada", "La sesion se ha cerrado satisfactoriamente");
    }
}
