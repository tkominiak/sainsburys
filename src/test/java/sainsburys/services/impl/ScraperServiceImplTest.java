package sainsburys.services.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.tomakehurst.wiremock.WireMockServer;

import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockResolver.Wiremock;
import sainsburys.model.Product;
import sainsburys.model.Products;
import sainsburys.services.ScraperService;
import sainsburys.services.impl.ScraperServiceImpl;

@ExtendWith({ WiremockResolver.class })
class ScraperServiceImplTest {

	private ScraperService scraperService = new ScraperServiceImpl();

	private static final String BASE_URL_PATH = "/berries-cherries-currants6039.html";

	@Test
	void testGetProductsWhenParamIsNull() {
		// Test
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			scraperService.getProducts(null);
		});
	}

	@Test
	void testGetProductsWhenParamIsFake(@Wiremock WireMockServer server) {
		// Test
		Assertions.assertThrows(IOException.class, () -> {
			scraperService.getProducts("http://0.0.0.0:" + server.port() + "/fake");
		});

		// Cleanup
		server.shutdown();
	}

	@Test
	void testGetProductsWhenNoProducts(@Wiremock WireMockServer server) throws Exception {
		// Setup
		Products expectedProducts = new Products(new ArrayList<>());
		mockProducts(server, expectedProducts);

		// Test
		Products actualProducts = scraperService.getProducts("http://0.0.0.0:" + server.port() + BASE_URL_PATH);

		// Verify
		Assertions.assertEquals(expectedProducts, actualProducts);

		// Cleanup
		server.shutdown();
	}

	@Test
	void testGetProductsWhenOneProduct(@Wiremock WireMockServer server) throws Exception {
		// Setup
		ArrayList<Product> list = new ArrayList<>();
		list.add(new Product("title0", 22.5, 5.0, "description0"));
		Products expectedProducts = new Products(list);
		mockProducts(server, expectedProducts);
		mockProductsDetails(server, 0, expectedProducts, true);

		// Test
		Products actualProducts = scraperService.getProducts("http://0.0.0.0:" + server.port() + BASE_URL_PATH);

		// Verify
		Assertions.assertEquals(expectedProducts, actualProducts);

		// Cleanup
		server.shutdown();
	}

	@Test
	void testGetProductsAll(@Wiremock WireMockServer server) throws Exception {
		// Setup
		ArrayList<Product> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Double kCalPer100G = (i % 2 == 0) ? null : i * 5.5;
			list.add(new Product("title" + i, kCalPer100G, i * 2.5, "description" + i));
		}
		Products expectedProducts = new Products(list);
		mockProducts(server, expectedProducts);
		for (int i = 0; i < 10; i++) {
			mockProductsDetails(server, i, expectedProducts, i % 3 == 0);
		}

		// Test
		Products actualProducts = scraperService.getProducts("http://0.0.0.0:" + server.port() + BASE_URL_PATH);

		// Verify
		Assertions.assertEquals(expectedProducts, actualProducts);

		// Cleanup
		server.shutdown();
	}

	private void mockProducts(WireMockServer server, Products products) {
		int index = 0;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<!DOCTYPE html>");
		stringBuilder.append("<html>");
		stringBuilder.append("    <body>");
		for (Product product : products.getProdukts()) {
			stringBuilder.append("    <div class=\"product\">");
			stringBuilder.append("        <a href=\"").append(getProductDetaildsLink(index++)).append("\">")
					.append(product.getTitle()).append("</a>");
			stringBuilder.append("        <p class=\"pricePerUnit\">£").append(product.getPricePerUnit())
					.append("</p>");
			stringBuilder.append("    </div>");
		}
		stringBuilder.append("    </body>");
		stringBuilder.append("</html>");

		server.stubFor(get(urlPathEqualTo(BASE_URL_PATH)).willReturn(aResponse().withStatus(HttpsURLConnection.HTTP_OK)
				.withHeader("Content-Type", "text/html").withBody(stringBuilder.toString())));
	}

	private String getProductDetaildsLink(int index) {
		return "/product.detaild.link" + index + ".html";
	}

	private void mockProductsDetails(WireMockServer server, int index, Products products,
			boolean hasDescriptionParagraph) {
		Product product = products.getProdukts().get(index);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<!DOCTYPE html>");
		stringBuilder.append("<html>");
		stringBuilder.append("    <body>");
		stringBuilder.append("        <div class=\"productText\">");
		if (hasDescriptionParagraph) {
			stringBuilder.append("        <p>Description</p>");
		}
		stringBuilder.append("            <p>").append(product.getDescription()).append("</p>");
		stringBuilder.append("        </div>");
		if (product.getkCalPer100G() != null) {
			stringBuilder.append("    <table class=\"nutritionTable\">");
			stringBuilder.append("        <tbody>");
			stringBuilder.append("            <tr>");
			stringBuilder.append(
					"                <th scope=\"row\" class=\"rowHeader\" rowspan=\"2\">Energy</th><td class=\"tableRow1\">133kJ</td><td class=\"tableRow1\">-</td>");
			stringBuilder.append("            </tr>");
			stringBuilder.append("            <tr>");
			stringBuilder.append("                <td class=\"tableRow0\">").append(product.getkCalPer100G())
					.append("kcal</td><td class=\"tableRow0\">2%</td>");
			stringBuilder.append("            </tr>");
			stringBuilder.append("        </tbody>");
			stringBuilder.append("    </table>");
		}
		stringBuilder.append("    </body>");
		stringBuilder.append("</html>");

		server.stubFor(get(urlPathEqualTo(getProductDetaildsLink(index)))
				.willReturn(aResponse().withStatus(HttpsURLConnection.HTTP_OK).withHeader("Content-Type", "text/html")
						.withBody(stringBuilder.toString())));
	}
}
