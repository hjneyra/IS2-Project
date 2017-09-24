package is2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.ProductRepository;
import repository.ShopRepository;
import domain.Product;
import domain.Shop;
import java.util.List;
import javax.annotation.PostConstruct;

@SpringBootApplication
@EntityScan("domain")
@EnableJpaRepositories("repository")
public class Is2projectApplication {

  @Autowired
  ProductRepository productRepository;
  @Autowired
  ShopRepository shopRepository;

  /* EXAMPLE:
    @PostConstruct
    void init() {
      LinioScraper linioScraper = new LinioScraper();
      List<Product> products = linioScraper.parseProducts();
      Shop shop = linioScraper.parseShop();
      // NOTE: shop must set the products to the products scraped,
      // because we don't have other way to connect them.
      shop.setProducts(products);
      productRepository.save(products) ;
      shopRepository.save(shop);
    }
  */
  public static void main(String[] args) {
    SpringApplication.run(Is2projectApplication.class, args);
  }
}
