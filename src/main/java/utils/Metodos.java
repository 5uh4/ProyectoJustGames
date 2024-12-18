package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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
	 * @param title        Titulo que queramos ponerle a la pantalla
	 * @param iconPath     Localizacion de la imagen de icono
	 */
	public static void cambiarPantalla(Stage currentStage, String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
			Parent root = loader.load();
			Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.setTitle("Just Games");
			newStage.getIcons().add(new Image("file:src/main/resources/JustGamesFaviconNoBG.png"));

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
	public static void mostrarLogin(Stage currentStage, String fxmlPath, String title, String iconPath) {
		try {
			FXMLLoader loader = new FXMLLoader(Metodos.class.getResource(fxmlPath));
			Parent root = loader.load();
			Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.setTitle(title);
			newStage.getIcons().add(new Image("file:src/main/resources/JustGamesFaviconNoBG.png"));
			
			newStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Metodo de cierre de sesion, se encuentra aqui puesto que se reutiliza tres
	 * veces en tres pantallas distintas
	 */
	public static void cierreDeSesion(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		Listas.userLogged = null;
		Metodos.cambiarPantalla(stage, "/views/VGDMain.fxml");
		Metodos.mostrarAlerta("Sesión cerrada", "La sesion se ha cerrado satisfactoriamente");
	}
}
