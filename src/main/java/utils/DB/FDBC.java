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
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

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
			DocumentReference docRef = db.collection("usuarios").document(username);
			docRef.set(usuario).get();
			System.out.println("Usuario actualizado exitosamente.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void eliminarUsuario(String username) {
		try {
			DocumentReference docRef = db.collection("usuarios").document(username);
			docRef.delete().get();
			System.out.println("Usuario eliminado exitosamente.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void crearVideojuego(Videogame videojuego) {
		try {
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
	
	public static List<Videogame> obtenerJuegosDeUsuario(String username) {
	    List<Videogame> juegosDelUsuario = new ArrayList<>();
	    try {
	        // 1. Recuperar al usuario desde Firebase
	        Users usuario = obtenerUsuario(username);

	        if (usuario == null) {
	            System.out.println("Error: El usuario con username '" + username + "' no existe.");
	            return juegosDelUsuario; // Lista vacía
	        }

	        // 2. Obtener la lista de IDs de videojuegos del usuario
	        List<Integer> juegosIds = usuario.getJuegosFavoritos();

	        // 3. Recuperar cada videojuego usando su ID
	        for (Integer id : juegosIds) {
	            Videogame juego = obtenerVideojuego(id);
	            if (juego != null) {
	                juegosDelUsuario.add(juego);
	            } else {
	                System.out.println("Advertencia: No se encontró el videojuego con ID " + id);
	            }
	        }

	        System.out.println("Se han obtenido " + juegosDelUsuario.size() + " videojuegos para el usuario '" + username + "'.");

	    } catch (Exception e) {
	        System.err.println("Error al obtener los videojuegos del usuario '" + username + "'.");
	        e.printStackTrace();
	    }
	    return juegosDelUsuario;
	}


	public static void actualizarVideojuego(int id, Videogame videojuego) {
		try {
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(id));
			docRef.set(videojuego).get();
			System.out.println("Videojuego actualizado exitosamente.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void eliminarVideojuego(int id) {
		try {
			DocumentReference docRef = db.collection("videogames").document(String.valueOf(id));
			docRef.delete().get();
			System.out.println("Videojuego eliminado exitosamente.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public static void añadirVideojuegoUsuario(String username, int videojuegoId) {
	    try {
	        // Obtén al usuario desde la base de datos
	        Users usuario = obtenerUsuario(username);

	        if (usuario == null) {
	            System.out.println("Error: El usuario con el username '" + username + "' no existe.");
	            return;
	        }

	        // Obtén la lista actual de juegos favoritos del usuario
	        List<Integer> juegosFavoritos = usuario.getJuegosFavoritos();

	        // Verifica si el videojuego ya está en la lista
	        if (juegosFavoritos.contains(videojuegoId)) {
	            System.out.println("El videojuego con ID " + videojuegoId + " ya está en la lista de favoritos del usuario " + username + ".");
	        } else {
	            // Añade el videojuego a la lista
	            juegosFavoritos.add(videojuegoId);
	            usuario.setJuegosFavoritos(juegosFavoritos);

	            crearVideojuego(RealSteamAPI.fetchSteamGameDetails(String.valueOf(videojuegoId)));
	            // Actualiza al usuario en la base de datos
	            actualizarUsuario(username, usuario);
	            System.out.println("El videojuego con ID " + videojuegoId + " se añadió a la lista de favoritos del usuario " + username + ".");
	        }
	    } catch (Exception e) {
	        System.err.println("Error al añadir el videojuego a la lista de favoritos del usuario.");
	        e.printStackTrace();
	    }
	}
	
	/**
     * Método para obtener todos los videojuegos almacenados en la base de datos.
     *
     * @return Lista de videojuegos.
     */
    public static List<Videogame> obtenerTodosLosVideojuegos() {
        List<Videogame> videojuegos = new ArrayList<>();

        try {
            // Referencia a la colección "videogames" en la base de datos
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference videojuegosRef = db.collection("videogames");

            // Obtener todos los documentos de la colección
            QuerySnapshot snapshot = videojuegosRef.get().get();

            // Convertir cada documento a un objeto Videogame y agregarlo a la lista
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

	public static void main(String[] args) {
		conectarFirebase();

		// Crear videojuegos
		Videogame juego1 = new Videogame();
		juego1.setTitle("The Witcher 3: Wild Hunt");
		juego1.setDescription("Aventura epica");
		juego1.setPrecio(59.99);
		juego1.setId(292030);

		Videogame juego2 = new Videogame();
		juego2.setTitle("Elden Ring");
		juego2.setDescription("Action RPG");
		juego2.setPrecio(49.99);
		juego2.setId(1245620);

		crearVideojuego(juego1);
		crearVideojuego(juego2);

		// Crear usuario
		Users usuario1 = new Users("user1", "password123", new ArrayList<>(List.of(juego1.getId(), juego2.getId())));
		crearUsuario(usuario1);

		// Obtener y actualizar usuario
		Users obtenidoUsuario = obtenerUsuario("user1");
		if (obtenidoUsuario != null) {
			System.out.println("Usuario obtenido: " + obtenidoUsuario);
			obtenidoUsuario.getJuegosFavoritos().remove((Integer) juego1.getId());
			actualizarUsuario("user1", obtenidoUsuario);
		}

		// Eliminar datos
		eliminarUsuario("user1");
		eliminarVideojuego(1);
		eliminarVideojuego(2);
	}
}
