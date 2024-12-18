package utils.DB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import models.Users;
import models.Videogame;
import utils.StoreData.RealSteamAPI;

/**
 * Clase para conectar con la base de datos de Firestore.
 * 
 * @author thesu
 */
public class FDBC {
	public static Firestore db;

	public static synchronized void conectarFirebase() {
		try {
			// Verificar si FirebaseApp ya está inicializado
			if (FirebaseApp.getApps().isEmpty()) {
				InputStream serviceAccount = FDBC.class.getClassLoader().getResourceAsStream("videogame-deals.json");

				if (serviceAccount == null) {
					throw new FileNotFoundException("No se encontró el archivo de credenciales de Firebase.");
				}

				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

				FirebaseApp.initializeApp(options);
				System.out.println("FirebaseApp inicializado correctamente.");
			}

			// Inicializar Firestore
			db = FirestoreClient.getFirestore();
			System.out.println("Conexión a Firestore exitosa.");
		} catch (IOException e) {
			System.err.println("Error al conectar a Firebase.");
			e.printStackTrace();
		}
	}

	private static void verificarConexion() {
		if (db == null) {
			conectarFirebase();
		}
	}

	public static void crearUsuario(Users usuario) {
		try {
			verificarConexion();
			DocumentReference docRef = db.collection("usuarios").document(usuario.getUsername());
			if (docRef.get().get().exists()) {
				System.out.println("Error: El usuario con el username " + usuario.getUsername() + " ya existe.");
			} else {
				docRef.set(usuario).get();
				System.out.println("Usuario creado exitosamente.");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static Users obtenerUsuario(String username) {
		try {
			verificarConexion();
			DocumentReference docRef = db.collection("usuarios").document(username);
			DocumentSnapshot document = docRef.get().get();

			if (document.exists()) {
				return document.toObject(Users.class);
			} else {
				System.out.println("No se encontró el usuario con el username: " + username);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void actualizarUsuario(String username, Users usuario) {
		try {
			verificarConexion();
			DocumentReference docRef = db.collection("usuarios").document(username);
			docRef.set(usuario).get();
			System.out.println("Usuario actualizado exitosamente.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void eliminarUsuario(String username) {
		try {
			verificarConexion();
			DocumentReference docRef = db.collection("usuarios").document(username);
			docRef.delete().get();
			System.out.println("Usuario eliminado exitosamente.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void crearVideojuego(Videogame videojuego) {
		try {
			verificarConexion();
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(videojuego.getId()));
			if (docRef.get().get().exists()) {
				System.out.println("Error: El videojuego con el ID " + videojuego.getId() + " ya existe.");
			} else {
				docRef.set(videojuego).get();
				System.out.println("Videojuego creado exitosamente.");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static Videogame obtenerVideojuego(int id) {
		try {
			verificarConexion();
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(id));
			DocumentSnapshot document = docRef.get().get();

			if (document.exists()) {
				return document.toObject(Videogame.class);
			} else {
				System.out.println("No se encontró el videojuego con el ID: " + id);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Videogame> obtenerTodosLosVideojuegos() {
		List<Videogame> videojuegos = new ArrayList<>();

		try {
			verificarConexion();
			CollectionReference videojuegosRef = db.collection("videogames");

			QuerySnapshot snapshot = videojuegosRef.get().get();
			snapshot.getDocuments().forEach(document -> {
				Videogame videojuego = document.toObject(Videogame.class);
				videojuegos.add(videojuego);
			});

			System.out.println("Se han obtenido " + videojuegos.size() + " videojuegos de la base de datos.");
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error al obtener los videojuegos de la base de datos.");
			e.printStackTrace();
		}

		return videojuegos;
	}

	}
