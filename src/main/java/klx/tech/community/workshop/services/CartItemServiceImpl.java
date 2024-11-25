package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.repositories.CartItemRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    @Transactional
    public CartItem create(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem update(Long id, Map<Long, Integer> updatedProducts) {
        // Buscar el CartItem existente
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        // Actualizar las cantidades de los productos
        cartItem.getCartItemProducts().forEach(cartItemProduct -> {
            Long productId = cartItemProduct.getProduct().getId();
            if (updatedProducts.containsKey(productId)) {
                cartItemProduct.setQuantity(updatedProducts.get(productId));
            }
        });

        // Guardar los cambios
        return cartItemRepository.save(cartItem);
    }


    @Override
    public ProductCartItemDTO toProductCartItemDTO(Product product, Integer quantity) {
    ProductDTO productDTO = productService.toProductDTO(product);
    return ProductCartItemDTO.builder()
            .product(productDTO)
            .quantity(quantity)
            .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new RuntimeException("CartItem not found with id: " + id);
        }
        cartItemRepository.deleteById(id);
    }

}
