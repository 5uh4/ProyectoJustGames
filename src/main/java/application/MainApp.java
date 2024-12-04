package application;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Videogame;

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
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		List<String> plataformas = new ArrayList<String>();
		plataformas.add("PlayStation 5");
		plataformas.add("PC");
		List<String> tiendas = new ArrayList<String>();
		tiendas.add("Steam");
		tiendas.add("PSN");
		Videogame v = new Videogame(1, "Mario Bros", "Juego de plataformas", plataformas, 59.99, tiendas);
		System.out.println(v.toJsonString());
	}

}
