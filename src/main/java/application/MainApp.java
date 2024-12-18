package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Cargo la vista
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(MainApp.class.getResource("/views/VGDMain.fxml"));

			// Cargo la ventana
			Pane ventana = (Pane) loader.load();

			Scene scene = new Scene(ventana);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Just Games");
			primaryStage.getIcons().add(new Image("file:src/resources/Just Games Favicon No Bg.png"));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		
	}

}
