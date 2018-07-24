package sainsburys.services;

import java.io.IOException;

import sainsburys.model.Products;

/**
 * An interface of the service to scraping.
 */
public interface ScraperService {

	/**
	 * Get products.
	 * 
	 * @param url an URL of the Sainsbury’s Groceries website to scraping.
	 * @return the {@link Products}.
	 * @throws IOException when something went wrong with scraping the page.
	 */
	Products getProducts(String url) throws IOException;
}
