package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDTO> findAll();
    Optional<ProductDTO> findById(Long id);
    ProductDTO toDTO(Product product);
}
