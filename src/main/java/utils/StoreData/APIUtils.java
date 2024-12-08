package utils.StoreData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Videogame;

public class APIUtils {

	// URLs base para las APIs.
    private static final String STEAM_SEARCH_API_URL = "https://store.steampowered.com/api/storesearch/?term=";
	private static final String STEAM_API_URL = "https://store.steampowered.com/api/appdetails?appids=";
	private static final String EPIC_SEARCH_API_URL = "https://store-content.ak.epicgames.com/api/content/v2/search";
	private static final String EPIC_DETAILS_API_URL = "https://store-content.ak.epicgames.com/api/content/v2/";

	/**
	 * Método para obtener los detalles de un juego desde la API de Steam.
	 */
	public static Videogame fetchSteamGameDetails(String gameId) {
		try {
			String apiUrl = STEAM_API_URL + gameId + "&cc=es&l=spanish";

			URI apiUri = new URI(apiUrl);
			URL apiURL = apiUri.toURL();

			HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONObject gameData = jsonResponse.getJSONObject(gameId).getJSONObject("data");

			String title = gameData.getString("name");
			String description = gameData.getString("short_description");
			double price = gameData.getJSONObject("price_overview").getDouble("final") / 100.0;
			List<String> platforms = gameData.has("platforms")
					? gameData.getJSONObject("platforms").toMap().keySet().stream().toList()
					: Collections.emptyList();

			return new Videogame(0, title, description, platforms, price, List.of("Steam"));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
     * Método para obtener el ID de un juego en Steam a partir de su nombre.
     */
    public static String fetchSteamGameId(String gameName) {
        try {
            String apiUrl = STEAM_SEARCH_API_URL + gameName.replace(" ", "+") + "&l=spanish";

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

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");

            if (items.length() > 0) {
                return items.getJSONObject(0).getString("id");
            } else {
                System.out.println("No se encontró ningún juego con el nombre especificado.");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método para guardar todos los videojuegos encontrados en Steam por un nombre.
     */
    public static List<Videogame> fetchSteamGamesList(String gameName) {
        try {
            String apiUrl = STEAM_SEARCH_API_URL + gameName.replace(" ", "+") + "&l=spanish";

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

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");

            List<Videogame> videogames = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject game = items.getJSONObject(i);

                String title = game.getString("name");
                String description = "Descripción no disponible en la búsqueda.";
                double price = game.has("price_overview") ? game.getJSONObject("price_overview").getDouble("final") / 100.0 : 0.0;
                List<String> platforms = game.has("platforms")
                        ? game.getJSONObject("platforms").toMap().keySet().stream().toList()
                        : Collections.emptyList();

                Videogame videogame = new Videogame(0, title, description, platforms, price, List.of("Steam"));
                videogames.add(videogame);
            }

            return videogames;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

	/**
	 * Método para buscar el ID del juego en Epic Games.
	 */
	public static String fetchEpicGameId(String gameName) {
		try {
			String apiUrl = EPIC_SEARCH_API_URL + "?query=" + gameName.replace(" ", "%20");

			URI apiURI = new URI(apiUrl);
			URL apiURL = apiURI.toURL();

			HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "es-ES,es;q=0.9");
			connection.setRequestProperty("Referer", "https://www.epicgames.com/store");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONArray elements = jsonResponse.getJSONArray("elements");

			if (elements.length() > 0) {
				JSONObject firstGame = elements.getJSONObject(0);
				return firstGame.getString("namespace");
			}

			return null;

		} catch (FileNotFoundException e) {
			System.err.println("URL no válida o recurso no encontrado: " + EPIC_SEARCH_API_URL);
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Método para buscar una lista de videojuegos en la API de Epic Games que
	 * coincidan con el nombre proporcionado.
	 */
	public static List<Videogame> fetchEpicGamesList(String gameName) {
		try {
			String apiUrl = EPIC_SEARCH_API_URL + "?query=" + gameName.replace(" ", "%20");

			URI apiURI = new URI(apiUrl);
			URL apiURL = apiURI.toURL();

			HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "es-ES,es;q=0.9");
			connection.setRequestProperty("Referer", "https://www.epicgames.com/store");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONArray elements = jsonResponse.getJSONArray("elements");

			// Procesar cada elemento del array
			List<Videogame> videogames = new ArrayList<>();
			for (int i = 0; i < elements.length(); i++) {
				JSONObject game = elements.getJSONObject(i);

				String title = game.getString("title");
				String description = game.optString("description", "No description available.");
				double price = game.has("price") ? game.getJSONObject("price").getDouble("totalPrice") / 100.0 : 0.0;

				Videogame videogame = new Videogame(0, title, description, List.of("PC"), price, List.of("Epic Games"));
				videogames.add(videogame);
			}

			return videogames;

		} catch (FileNotFoundException e) {
			System.err.println("URL no válida o recurso no encontrado: " + EPIC_SEARCH_API_URL);
			e.printStackTrace();
			return Collections.emptyList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * Método para obtener los detalles de un juego desde Epic Games por ID.
	 */
	public static Videogame fetchEpicGameDetails(String gameId) {
		try {
			String apiUrl = EPIC_DETAILS_API_URL + gameId;

			URI apiURI = new URI(apiUrl);
			URL apiURL = apiURI.toURL();

			HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONObject game = jsonResponse.getJSONObject("page").getJSONObject("data");

			String title = game.getString("title");
			String description = game.optString("description", "No description available.");
			double price = game.has("price") ? game.getJSONObject("price").getDouble("totalPrice") / 100.0 : 0.0;

			return new Videogame(0, title, description, List.of("PC"), price, List.of("Epic Games"));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		// Ejemplo de uso con la API de Steam.
		String steamGameId = "1091500"; // ID de Cyberpunk 2077.
		Videogame steamGame = fetchSteamGameDetails(steamGameId);
		if (steamGame != null) {
			System.out.println("Detalles del juego en Steam:");
			System.out.println(steamGame);
		}

		// Ejemplo de uso con la API de Epic Games.
		String epicGameName = "Cyberpunk 2077";
		String epicGameId = fetchEpicGameId(epicGameName);
		if (epicGameId != null) {
			Videogame epicGame = fetchEpicGameDetails(epicGameId);
			if (epicGame != null) {
				System.out.println("Detalles del juego en Epic Games:");
				System.out.println(epicGame);
			} else {
				System.out.println("No se pudieron obtener los detalles del juego en Epic Games.");
			}
		} else {
			System.out.println("No se encontró el ID del juego en Epic Games.");
		}
	}
}
