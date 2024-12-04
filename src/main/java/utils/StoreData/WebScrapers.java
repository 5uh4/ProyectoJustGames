package utils.StoreData;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import models.Videogame;

public class WebScrapers {

	// URLs base para cada tienda.
	private static final String STEAM_SEARCH_URL = "https://store.steampowered.com/search/?l=spanish&term=";
	private static final String EPIC_SEARCH_URL = "https://store.epicgames.com/es-ES/browse?q=";
	private static final String PLAYSTATION_SEARCH_URL = "https://store.playstation.com/es-es/search/";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";

	/**
	 * Método general para buscar juegos en una tienda específica y devolver la URL
	 * del primer resultado.
	 */
	public static String searchGame(String gameName, String storeName) throws IOException {
		String searchUrl = "";
		String gameUrl = null;

		switch (storeName.toLowerCase()) {
		case "steam":
			searchUrl = STEAM_SEARCH_URL + gameName.replace(" ", "+");
			break;
		case "epic games":
			searchUrl = EPIC_SEARCH_URL + gameName.replace(" ", "%20");
			break;
		case "playstation store":
			searchUrl = PLAYSTATION_SEARCH_URL + gameName.replace(" ", "%20");
			break;
		default:
			throw new IllegalArgumentException("Store no soportada: " + storeName);
		}

		Document doc = Jsoup.connect(searchUrl).userAgent(USER_AGENT).timeout(10 * 1000).get();
		Element gameLink = getGameLink(doc, storeName);

		if (gameLink != null) {
			gameUrl = gameLink.attr("href");
			if (storeName.equalsIgnoreCase("epic games")) {
				gameUrl = "https://store.epicgames.com" + gameUrl;
			} else if (storeName.equalsIgnoreCase("playstation store") && !gameUrl.startsWith("http")) {
				gameUrl = "https://store.playstation.com" + gameUrl;
			}
		}

		return gameUrl;
	}

	// Método auxiliar para obtener el enlace del juego según la tienda.
	private static Element getGameLink(Document doc, String storeName) {
		switch (storeName.toLowerCase()) {
		case "steam":
			return doc.select(".search_result_row").first();
		case "epic games":
			return doc.select(".css-1jx3eyg a").first();
		case "playstation store":
			return doc.select(".psw-content-link").first();
		default:
			return null;
		}
	}

	private static String getTextSafely(Element element) {
		return element != null ? element.text() : "";
	}

	/**
	 * Método para extraer los detalles del juego desde una URL específica y la
	 * tienda.
	 */
	public static Videogame scrapeGameDetails(String gameUrl, String storeName) throws IOException {
		Document doc = Jsoup.connect(gameUrl).userAgent(USER_AGENT).timeout(10 * 10000).get();
		String title = "", description = "", price = "";
		List<String> plataformas = Collections.emptyList();

		switch (storeName.toLowerCase()) {
		case "steam":
			title = getTextSafely(doc.select(".apphub_AppName").first());
			description = getTextSafely(doc.select(".game_description_snippet").first());
			price = getTextSafely(doc.select(".game_purchase_price, .discount_final_price").first()).replace("€", "")
					.trim();
			plataformas = doc.select(".game_area_details_specs .name").eachText();
			break;

		case "epic games":
			doc = Jsoup.connect(gameUrl).userAgent(USER_AGENT).referrer("https://www.google.com")
					.header("Accept-Language", "es-ES,es;q=0.9").timeout(10 * 1000).get();

			title = getTextSafely(doc.select(".css-1cnp3b9").first());
			description = getTextSafely(doc.select(".css-1v3h90q p").first());
			price = getTextSafely(doc.select(".css-16q9pr7").first()).replace("€", "").trim();
			plataformas = List.of("PC");
			break;

		case "playstation store":
			title = getTextSafely(doc.select(".psw-t-title-m").first());
			description = getTextSafely(doc.select(".psw-t-body").first());
			price = getTextSafely(doc.select(".psw-t-title-l").first()).replace("€", "").trim();
			plataformas = List.of("PlayStation");
			break;
		default:
			throw new IllegalArgumentException("Tienda no soportada: " + storeName);
		}

		double parsedPrice = price.isEmpty() ? 0.0 : Double.parseDouble(price.replace(",", "."));
		return new Videogame(0, title, description, plataformas, parsedPrice, List.of(storeName));
	}

	/**
	 * ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	 * 
	 * scheduler.scheduleAtFixedRate(() -> { try { // Llama a tus métodos de
	 * scraping y actualización. } catch (Exception e) { e.printStackTrace(); } },
	 * 0, 7, TimeUnit.DAYS);
	 */

	public static void main(String[] args) {
		try {
			String gameName = "Cyberpunk 2077";

			// Búsqueda y scraping en Steam.
			String steamUrl = WebScrapers.searchGame(gameName, "steam");
			if (steamUrl != null) {
				Videogame steamGame = WebScrapers.scrapeGameDetails(steamUrl, "steam");
				System.out.println(steamGame);
			}

			// Búsqueda y scraping en Epic Games.
			String epicUrl = WebScrapers.searchGame(gameName, "epic games");
			if (epicUrl != null) {
				Videogame epicGame = WebScrapers.scrapeGameDetails(epicUrl, "epic games");
				System.out.println(epicGame);
			}

			// Búsqueda y scraping en PlayStation Store.
			String psUrl = WebScrapers.searchGame(gameName, "playstation store");
			if (psUrl != null) {
				Videogame psGame = WebScrapers.scrapeGameDetails(psUrl, "playstation store");
				System.out.println(psGame);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
