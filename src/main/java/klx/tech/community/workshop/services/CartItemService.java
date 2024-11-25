package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CartItemService {
    List<CartItem> findAll();
    Optional<CartItem> findById(Long id);
    CartItem create(CartItem cartItem);
    CartItem update(Long id, Map<Long, Integer> updatedProducts);
    void delete(Long id);
    ProductCartItemDTO toProductCartItemDTO(Product product, Integer quantity);
}
