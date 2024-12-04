package utils.DB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import models.Users;
import models.Videogame;

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
			InputStream serviceAccount = FDBC.class.getClassLoader().getResourceAsStream("videogame-deals.json");

			if (serviceAccount == null) {
			    throw new FileNotFoundException("No se encontro el archivo de credenciales de Firebase.");
			}

			FirebaseOptions options = FirebaseOptions.builder()
			        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			        .build();

			FirebaseApp.initializeApp(options);

			db = FirestoreClient.getFirestore();

			System.out.println("Conectado exitoso");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void crearUsuario(Users usuario) {
	    try {
	        DocumentReference docRef = db.collection("usuarios").document(usuario.getUsername());
	        ApiFuture<DocumentSnapshot> future = docRef.get();
	        DocumentSnapshot document = future.get();

	        if (document.exists()) {
	            System.out.println("Error: El usuario con el username " + usuario.getUsername() + " ya existe.");
	        } else {
	            ApiFuture<WriteResult> writeFuture = docRef.set(usuario);
	            System.out.println("Usuario creado: " + writeFuture.get().getUpdateTime());
	        }
	    } catch (InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	    }
	}


	public static Users obtenerUsuario(String username) {
		try {
			DocumentReference docRef = db.collection("usuarios").document(username);
			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();

			if (document.exists()) {
				Users usuario = document.toObject(Users.class);
				return usuario;
			} else {
				System.out.println("No se encontrÃ³ el usuario con el username: " + username);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void actualizarUsuario(String username, Users usuario) {
		try {
			DocumentReference docRef = db.collection("usuarios").document(username);
			ApiFuture<WriteResult> future = docRef.set(usuario);

			System.out.println("Usuario actualizado: " + future.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void eliminarUsuario(String username, Users usuario) {
		try {
			DocumentReference docRef = db.collection("usuarios").document(username);
			ApiFuture<WriteResult> future = docRef.delete();

			System.out.println("Usuario actualizado: " + future.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void crearVideojuego(Videogame videojuego) {
	    try {
	        DocumentReference docRef = db.collection("videogames").document(String.valueOf(videojuego.getId()));
	        ApiFuture<DocumentSnapshot> future = docRef.get();
	        DocumentSnapshot document = future.get();

	        if (document.exists()) {
	            System.out.println("Error: El videojuego con el id " + videojuego.getId() + " ya existe.");
	        } else {
	            ApiFuture<WriteResult> writeFuture = docRef.set(videojuego);
	            System.out.println("Videojuego creado: " + writeFuture.get().getUpdateTime());
	        }
	    } catch (InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	    }
	}


	public static Videogame obtenerVideojuego(int id) {
		try {
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(id));
			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();

			if (document.exists()) {
				Videogame videojuego = document.toObject(Videogame.class);
				return videojuego;
			} else {
				System.out.println("No se encontrÃ³ el videojuego con el id: " + id);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void actualizarVideojuego(int id, Videogame videojuego) {
		try {
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(id));
			ApiFuture<WriteResult> future = docRef.set(videojuego);

			System.out.println("Videojuego actualizado: " + future.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void eliminarVideojuego(int id) {
		try {
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(id));
			ApiFuture<WriteResult> future = docRef.delete();

			System.out.println("Videojuego eliminado: " + future.get().getUpdateTime());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	    conectarFirebase();

	    // ** PRUEBAS CON VIDEOJUEGOS **
	    System.out.println("=== Pruebas con Videojuegos ===");

	    // Crear videojuegos
	    Videogame juego1 = new Videogame(101, "Zelda: Breath of the Wild", "Aventura épica",
	                                         List.of("Switch"), 59.99, List.of("Amazon", "eShop"));
	    Videogame juego2 = new Videogame(102, "Elden Ring", "Acción RPG",
	                                         List.of("PC", "PS5", "Xbox"), 69.99, List.of("Steam", "Amazon"));

	    crearVideojuego(juego1);
	    crearVideojuego(juego2);

	    // Obtener videojuego
	    Videogame obtenidoJuego = obtenerVideojuego(101);
	    if (obtenidoJuego != null) {
	        System.out.println("Videojuego obtenido: " + obtenidoJuego);
	    }

	    // Actualizar videojuego
	    juego1.setPrecio(49.99);
	    actualizarVideojuego(101, juego1);

	    // Eliminar videojuego
	    eliminarVideojuego(101);

	    // ** PRUEBAS CON USUARIOS **
	    System.out.println("\n=== Pruebas con Usuarios ===");

	    // Crear usuarios
	    Users usuario1 = new Users("user1", "password123", new ArrayList<>(List.of(juego2)));
	    Users usuario2 = new Users("user2", "securepass456", new ArrayList<>(List.of(juego1, juego2)));

	    crearUsuario(usuario1);
	    crearUsuario(usuario2);

	    // Obtener usuario
	    Users obtenidoUsuario = obtenerUsuario("user1");
	    if (obtenidoUsuario != null) {
	        System.out.println("Usuario obtenido: " + obtenidoUsuario);
	    }

	    // Actualizar usuario (agregar un juego a favoritos)
	    usuario1.getJuegosFavoritos().add(juego1);
	    actualizarUsuario("user1", usuario1);

	    // Obtener usuario actualizado
	    obtenidoUsuario = obtenerUsuario("user1");
	    if (obtenidoUsuario != null) {
	        System.out.println("Usuario actualizado: " + obtenidoUsuario);
	    }

	    // Eliminar usuario
	    eliminarUsuario("user1", usuario1);

	    // Intentar obtener el usuario eliminado
	    obtenerUsuario("user1");
	    
//	    Borrar los usuarios de prueba
	    eliminarUsuario("user2", obtenerUsuario("user2"));
	    eliminarVideojuego(102);
	}



}
