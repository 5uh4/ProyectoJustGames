package controller;

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

public class VGDLoginController {

	@FXML
	private Button loginBtn;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button registerBtn;
	
	@FXML
	private TextField emailTF;
	
	@FXML
	private PasswordField passTF;
	
	
	@FXML
	private void logIn(ActionEvent event) {
		String email = emailTF.getText().trim();
        String password = passTF.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Metodos.mostrarAlerta("Error", "Por favor, completa todos los campos.");
            return;
        }

        Users usuario = FDBC.obtenerUsuario(email);
        if (usuario != null && usuario.getPassword().equals(password)) {
            Listas.userLogged = usuario; // Guardar el usuario logueado
            Metodos.mostrarAlerta("Éxito", "Inicio de sesión exitoso.");
            
            // Cerrar solo la ventana de login
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.close();
        } else {
            Metodos.mostrarAlerta("Error", "Credenciales incorrectas.");
        }
	}
	
	@FXML
	private void register(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		Metodos.cambiarPantalla(stage, "/views/VGDRegister.fxml");
	}
	
	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
		Metodos.cambiarPantalla(stage, "/views/VGDMain.fxml");
	}
}
