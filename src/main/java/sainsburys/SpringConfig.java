package sainsburys;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import sainsburys.services.ScraperService;
import sainsburys.services.impl.ScraperServiceImpl;

@Configuration
public class SpringConfig {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringConfig.class);
	private static final String URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

	@Bean
	public ScraperService getScraperService() {
		return new ScraperServiceImpl();
	}
	
	@Bean
	public CommandLineRunner run(ScraperService scraperService) throws IOException {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			LOG.info(mapper.writeValueAsString(scraperService.getProducts(URL)));
		};
	}
}
