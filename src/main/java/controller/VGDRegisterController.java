package controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Users;
import utils.Listas;
import utils.Metodos;
import utils.DB.FDBC;

public class VGDRegisterController {

	@FXML
	private Button registerBtn;

	@FXML
	private Button cancelBtn;

	@FXML
	private Button loginBtn;
	
	@FXML
	private TextField emailTF;

	@FXML
	private PasswordField passTF;

	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		Metodos.cambiarPantalla(stage, "/views/VGDMain.fxml");
	}

	@FXML
	private void volverLogin(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		Metodos.cambiarPantalla(stage, "/views/VGDLogin.fxml");
	}

	@FXML
	private void register(ActionEvent event) {
		String email = emailTF.getText().trim();
		String password = passTF.getText().trim();

		if (email.isEmpty() || password.isEmpty()) {
			Metodos.mostrarAlerta("Error", "Por favor, completa todos los campos.");
			return;
		}

		// Verificar si el usuario ya existe
		Users usuarioExistente = FDBC.obtenerUsuario(email);

		if (usuarioExistente != null) {
			Metodos.mostrarAlerta("Error", "El usuario ya existe. Elige otro nombre de usuario.");
			return;
		} else {
			// Crear un nuevo usuario
			Users nuevoUsuario = new Users();
			nuevoUsuario.setUsername(email);
			nuevoUsuario.setPassword(password);
			nuevoUsuario.setJuegosFavoritos(new ArrayList<>()); // Inicializa la lista vacía

			FDBC.crearUsuario(nuevoUsuario);
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			Metodos.mostrarAlerta("Usuario creado", "Usuario creado correctamente");
			Metodos.cambiarPantalla(stage, "/views/VGDMain.fxml");
			Listas.userLogged = nuevoUsuario;
		}

	}
}
