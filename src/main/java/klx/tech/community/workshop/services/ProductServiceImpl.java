package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.repositories.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<ProductDTO> findAllDTO() {
        return productRepository.findAll().stream()
                .map(this::toProductDTO) 
                .toList();
    }

    @Override
    public Optional<ProductDTO> findByIdDTO(Long id) {
        return productRepository.findById(id)
            .map(this::toProductDTO); 
    }

    @Override
    public ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .favorite(product.getFavorite())
                .imageUrl(buildImageUrl(product.getImageUrl())) // Pass the image_url field
                .build();
    }

    private String buildImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            // Default placeholder or null if no image is available
            return "http://localhost:8080/images/default.txt";
        }
        return String.format("http://localhost:8080/images/%s", imageUrl);
    }

}
