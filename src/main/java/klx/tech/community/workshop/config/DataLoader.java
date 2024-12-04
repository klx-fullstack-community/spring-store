package klx.tech.community.workshop.config;

import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Load example products
        if (productRepository.count() == 0) {
            Product product1 = new Product(
                    "Grey Jacket",
                    "Grey cloth jacket",
                    49.99,
                    "grey_jacket_base64.txt",
                    0.0,
                    false
            );

            Product product2 = new Product(
                    "Corduroy Pants",
                    "Dark green corduroy pants",
                    29.99,
                    "corduroy_pants_base64.txt",
                    0.0,
                    false
            );

            Product product3 = new Product(
                    "Fuchsia t-Shirt",
                    "Fuchsia-colored cotton T-shirt",
                    14.99,
                    "fuchsia_tshirt_base64.txt",
                    0.0,
                    false
            );

            Product product4 = new Product(
                    "*Polo Shirt",
                    "Navy blue polo with white striped collar and sleeves",
                    44.99,
                    "polo_image_base64.txt",
                    0.0,
                    false
            );

            Product product5 = new Product(
                    "Orange Dress",
                    "Orange dress with cobalt blue and white patterned details",
                    79.99,
                    "orange_dress_base64.txt",
                    0.0,
                    false
            );

            Product product6 = new Product(
                    "Purple Cap",
                    "Stylish and minimalist purple cap with a curved brim and smooth texture",
                    24.99,
                    "purple_cap_base64.txt",
                    0.0,
                    false
            );

            // Save data in database
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);
            productRepository.save(product6);

            // Show initialize producte in console
            System.out.println("Productos inicializados:");
            productRepository.findAll().forEach(product ->
                    System.out.println(product.getName() + " - $" + product.getPrice())
            );
        }
    }
}
