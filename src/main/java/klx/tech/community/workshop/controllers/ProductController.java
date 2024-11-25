package klx.tech.community.workshop.controllers;

import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.services.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products as a list of ProductDTO.
     *
     * @return A list of ProductDTO.
     */
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a product by its ID as a ProductDTO.
     *
     * @param id The ID of the product to retrieve.
     * @return The ProductDTO with the given ID, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
