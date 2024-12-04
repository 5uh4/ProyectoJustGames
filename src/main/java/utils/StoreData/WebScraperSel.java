package utils.StoreData;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import models.Videogame;

public class WebScraperSel {
	private static final String CHROME_DRIVER_PATH = "C:/Users/Usuario/git/chrome-win64/chrome.exe"; // Cambia a la ruta de tu chromedriver

	public static WebDriver getHeadlessDriver() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless"); // Modo sin cabeza
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920x1080");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-extensions");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");

		return new ChromeDriver(options);
	}

	public static Videogame scrapeEpicGames(String gameName) {
		WebDriver driver = getHeadlessDriver();
		try {
			String searchUrl = "https://store.epicgames.com/es-ES/browse?q=" + gameName.replace(" ", "%20");
			driver.get(searchUrl);

			// Buscar el primer enlace del juego.
			WebElement gameLink = driver.findElement(By.cssSelector(".css-1jx3eyg a"));
			if (gameLink != null) {
				String gameUrl = "https://store.epicgames.com" + gameLink.getAttribute("href");
				driver.get(gameUrl);

				// Extraer información del juego.
				String title = driver.findElement(By.cssSelector(".css-1cnp3b9")).getText();
				String description = driver.findElement(By.cssSelector(".css-1v3h90q p")).getText();
				String price = driver.findElement(By.cssSelector(".css-16q9pr7")).getText().replace("€", "").trim();

				double parsedPrice = Double.parseDouble(price.replace(",", "."));
				List<String> plataformas = List.of("PC");
				List<String> tiendas = List.of("Epic Games");

				return new Videogame(0, title, description, plataformas, parsedPrice, tiendas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit(); // Cierra el navegador después de usarlo.
		}
		return null;
	}

	public static void main(String[] args) {
		Videogame game = scrapeEpicGames("Cyberpunk 2077");
		if (game != null) {
			System.out.println(game);
		} else {
			System.out.println("No se pudo obtener la información del juego.");
		}
	}
}
