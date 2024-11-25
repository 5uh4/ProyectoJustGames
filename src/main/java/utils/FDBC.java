package utils;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

/**
 * Clase para conectar con la base de datos de firestore
 * 
 * @author thesu
 *
 */

public class FDBC {
	// https://console.firebase.google.com/u/0/project/videogame-deals/overview?hl=es-419

	public static Firestore db;

	public static void conectarFirebase() {
		try {
			FileInputStream serviceAccount = new FileInputStream("C:/Users/Usuario/git/ProyectoJustGames/src/main/java/utils/videogame-deals.json");
			
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

			FirebaseApp.initializeApp(options);
			
			db = FirestoreClient.getFirestore();
			
			System.out.println("Conectado exitoso");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		conectarFirebase();
	}
}
