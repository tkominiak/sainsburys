package sainsburys.services.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sainsburys.model.Product;
import sainsburys.model.Products;
import sainsburys.services.ScraperService;

/**
 * The implementation of {@link ScraperService} to scraping a portion of the Sainsbury’s Groceries website.
 */
@Service
public class ScraperServiceImpl implements ScraperService {

	private static final Logger LOG = LoggerFactory.getLogger(ScraperServiceImpl.class);

	@Override
	public Products getProducts(String url) throws IOException {
		
		ArrayList<Product> products = new ArrayList<Product>();

		Document mainSoup = Jsoup.connect(url).get();

		for (Element productElement : mainSoup.getElementsByClass("product")) {
			String title = productElement.selectFirst("a").ownText();
			
			Element pricingPerUnitElement = productElement.getElementsByClass("pricePerUnit").first();
			Double pricePerUnit = getDoubleFromString(pricingPerUnitElement.ownText());

			String linkToProductDetails = productElement.selectFirst("a").absUrl("href");
			LOG.info(linkToProductDetails);

			Document productSoup = Jsoup.connect(linkToProductDetails).get();
			Double kCalPer100G = getKCallPer100G(productSoup);
			String description = getDescription(productSoup);

			Product product = new Product(title, kCalPer100G, pricePerUnit, description);

			LOG.info(product.toString());

			products.add(product);
		}

		return new Products(products);
	}

	private String getDescription(Document productSoup) {
		String description;
		Element productTextElement = productSoup.selectFirst(".productText");
		if (productTextElement.child(0) != null && productTextElement.child(0).text().equals("Description")) {
			description = productTextElement.child(1).text();
		} else {
			description = productTextElement.text();
		}
		return description;
	}

	private Double getKCallPer100G(Document productSoup) {
		Double kCalPer100G = null;
		Element nutritionTableElement = productSoup.selectFirst(".nutritionTable");
		if (nutritionTableElement != null) {
			String nutrition = nutritionTableElement.selectFirst("table > tbody > tr:nth-child(2) > td").text();
			kCalPer100G = getDoubleFromString(nutrition);
		}
		return kCalPer100G;
	}

	private static Double getDoubleFromString(String s) {
		String numberString = s.replaceAll("[^0-9.]", "");
		return Double.parseDouble(numberString);
	}
}
