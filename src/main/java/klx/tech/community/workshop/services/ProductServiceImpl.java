package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.repositories.ProductRepository;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String IMAGE_DIRECTORY = "images"; // Directory for image files

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::toDTO);
    }

    /**
     * Converts a Product entity to ProductDTO.
     *
     * @param product The Product entity.
     * @return The corresponding ProductDTO.
     */
    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
    
        // Safely handle imageUrl and generate full file path
        if (product.getImageUrl() != null) {
            String filePath = Paths.get(IMAGE_DIRECTORY, product.getImageUrl()).toString(); // Generate full path
            dto.setImageBase64(readBase64FromFile(filePath)); // Read Base64 content
        } else {
            dto.setImageBase64(null); // Set as null if imageUrl is null
        }
    
        dto.setDiscount(product.getDiscount());
        dto.setFavorite(product.getFavorite());
        return dto;
    }

    /**
     * Reads the Base64 content from a file.
     *
     * @param filePath The path of the file to read.
     * @return The Base64 string contained in the file, or null if the file does not exist.
     */
    private String readBase64FromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                return Files.readString(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Base64 content from file: " + e.getMessage());
        }
        return null;
    }

}
