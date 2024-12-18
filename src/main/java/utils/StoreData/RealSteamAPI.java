package utils.StoreData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Videogame;

public class RealSteamAPI {

	private static final String STEAM_API_URL = "https://store.steampowered.com/api/appdetails?appids=";
	private static final String STEAM_APP_LIST_API = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";

	/**
	 * Método genérico para realizar solicitudes GET a las APIs.
	 */
	private static String getApiResponse(String apiUrl) {
		try {
			URI apiURI = new URI(apiUrl);
			URL apiURL = apiURI.toURL();
			HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "es-ES,es;q=0.9");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			// Loguear la respuesta para verificar qué datos se devuelven
//			System.out.println("Respuesta de la API (" + apiUrl + "): " + response);
			return response.toString();
		} catch (Exception e) {
			System.err.println("Error al realizar la solicitud GET: " + apiUrl);
			e.printStackTrace();
			return null;
		}
	}

	public static List<Integer> fetchGamesByName(String searchQuery) {
		List<Integer> matchingGameIds = new ArrayList<>();
		try {
			// Llama a la API y obtiene la respuesta
			String response = getApiResponse(STEAM_APP_LIST_API);

			if (response != null) {
				JSONObject jsonResponse = new JSONObject(response);
				JSONArray apps = jsonResponse.getJSONObject("applist").getJSONArray("apps");

				// Normalizar el término de búsqueda
				String normalizedQuery = normalizeText(searchQuery);

				// Busca juegos cuyo nombre contenga el término de búsqueda
				for (int i = 0; i < apps.length(); i++) {
					JSONObject app = apps.getJSONObject(i);
					String name = app.getString("name");
					String normalizedName = normalizeText(name);

					// Verifica si el nombre contiene el término de búsqueda normalizado
					if (normalizedName.contains(normalizedQuery)) {
						int appid = app.getInt("appid");
						matchingGameIds.add(appid); // Almacena solo el ID
					}
				}

				if (matchingGameIds.isEmpty()) {
					System.out.println("No se encontraron juegos que coincidan con: " + searchQuery);
				}
			} else {
				System.err.println("La respuesta de la API fue nula.");
			}
		} catch (Exception e) {
			System.err.println("Error al buscar juegos por nombre.");
			e.printStackTrace();
		}
		return matchingGameIds;
	}

	/**
	 * Método para normalizar texto eliminando acentos, caracteres especiales y
	 * espacios extra.
	 */
	private static String normalizeText(String text) {
		if (text == null)
			return "";
		// Convierte a minúsculas, elimina espacios extra y normaliza caracteres
		// especiales
		return java.text.Normalizer.normalize(text.toLowerCase().trim(), java.text.Normalizer.Form.NFD)
				.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // Elimina acentos
				.replaceAll("[^a-z0-9 ]", ""); // Elimina caracteres especiales excepto letras, números y espacios
	}

	public static Videogame fetchSteamGameDetails(String gameId) {
		String apiUrl = STEAM_API_URL + gameId;
		String response = getApiResponse(apiUrl);

		if (response != null) {
			try {
				JSONObject jsonResponse = new JSONObject(response);
				JSONObject gameData = jsonResponse.optJSONObject(gameId).optJSONObject("data");

				if (gameData != null) {
					String title = gameData.optString("name", "Título no disponible");
					String description = gameData.optString("short_description", "Descripción no disponible");

					// Obtén el precio

					double price = gameData.optJSONObject("price_overview") != null
							? gameData.getJSONObject("price_overview").optDouble("final", 0.0) / 100.0
							: 0.0;

					// Obtén la imagen del encabezado
					String image = gameData.optString("header_image", "");

					return new Videogame(Integer.parseInt(gameId), title, description, price, image);
				}
			} catch (Exception e) {
				System.err.println("Error al procesar los detalles del juego para el ID: " + gameId);
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Método para buscar videojuegos por nombre y obtener una lista de objetos
	 * Videogame con todos los detalles.
	 */
	public static List<Videogame> searchGamesByName(String searchQuery) {
		List<Videogame> foundGames = new ArrayList<>();
		try {
			List<Integer> gameIds = fetchGamesByName(searchQuery);

			if (!gameIds.isEmpty()) {
				for (int appid : gameIds) {
					Videogame game = fetchSteamGameDetails(String.valueOf(appid));
					if (game != null) {
						foundGames.add(game);
					}
				}
			} else {
				System.out.println("No se encontraron juegos con el nombre especificado.");
			}
		} catch (Exception e) {
			System.err.println("Error en la búsqueda de juegos.");
			e.printStackTrace();
		}
		return foundGames;
	}

	public static void main(String[] args) {
		   try {
	            // Prueba el método de búsqueda
	            List<Videogame> juegosEncontrados = searchGamesByName("Cyberpunk 2077");

	            // Mostrar resultados
	            System.out.println("Juegos encontrados:");
	            for (Videogame game : juegosEncontrados) {
	                System.out.println(game);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
