package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.CartItemDTO;
import klx.tech.community.workshop.dto.CartItemRequestDTO;
import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.CartItemProduct;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.repositories.CartItemRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public CartItem create(CartItemRequestDTO request) {
        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Invalid input: No products provided.");
        }

        CartItem cartItem = new CartItem();
        Set<CartItemProduct> cartItemProducts = request.getProducts().entrySet().stream().map(entry -> {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productService.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found for productId: " + productId));

            CartItemProduct cartItemProduct = new CartItemProduct();
            cartItemProduct.setProduct(product);
            cartItemProduct.setQuantity(quantity);
            cartItemProduct.setCartItem(cartItem);
            return cartItemProduct;
        }).collect(Collectors.toSet());

        cartItem.setCartItemProducts(cartItemProducts);
        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem update(Long id, CartItemRequestDTO request) {
        // Fetch the CartItem from the database
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        // Process the "products" map from the request
        Map<Long, Integer> updatedProducts = request.getProducts();

        // Check if the "products" map is empty or will result in no products remaining
        if (updatedProducts == null || updatedProducts.isEmpty()) {
            cartItem.getCartItemProducts().clear(); // Clear all products if the map is empty
            return cartItemRepository.save(cartItem);
        }

        // Remove products not present in the request or with invalid quantities
        cartItem.getCartItemProducts().removeIf(cartItemProduct -> {
            Long productId = cartItemProduct.getProduct().getId();
            return !updatedProducts.containsKey(productId) || updatedProducts.get(productId) <= 0;
        });

        // Update quantities for existing products in the CartItem
        cartItem.getCartItemProducts().forEach(cartItemProduct -> {
            Long productId = cartItemProduct.getProduct().getId();
            if (updatedProducts.containsKey(productId)) {
                cartItemProduct.setQuantity(updatedProducts.get(productId));
            }
        });

        // Add new products from the request that are not already in the CartItem
        updatedProducts.forEach((productId, quantity) -> {
            boolean exists = cartItem.getCartItemProducts().stream()
                    .anyMatch(cartItemProduct -> cartItemProduct.getProduct().getId().equals(productId));
            if (!exists && quantity > 0) {
                Product product = productService.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
                CartItemProduct newCartItemProduct = new CartItemProduct();
                newCartItemProduct.setCartItem(cartItem);
                newCartItemProduct.setProduct(product);
                newCartItemProduct.setQuantity(quantity);
                cartItem.getCartItemProducts().add(newCartItemProduct);
            }
        });

        return cartItemRepository.save(cartItem);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new RuntimeException("CartItem not found with id: " + id);
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    public ProductCartItemDTO findById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        if (cartItem.getCartItemProducts().isEmpty()) {
            throw new RuntimeException("CartItem has no associated products.");
        }

        CartItemProduct cartItemProduct = cartItem.getCartItemProducts().iterator().next();
        Product product = cartItemProduct.getProduct();
        Integer quantity = cartItemProduct.getQuantity();

        return toProductCartItemDTO(product, quantity);
    }

    @Override
    public List<ProductCartItemDTO> findAll() {
    
    List<CartItem> cartItems = cartItemRepository.findAll();

    return cartItems.stream()
            .filter(cartItem -> !cartItem.getCartItemProducts().isEmpty()) // Exclude CartItems with no products
            .flatMap(cartItem -> cartItem.getCartItemProducts().stream())
            .map(cartItemProduct -> {
                Product product = cartItemProduct.getProduct();
                Integer quantity = cartItemProduct.getQuantity();
                return toProductCartItemDTO(product, quantity);
            })
            .collect(Collectors.toList());
    }

    private ProductCartItemDTO toProductCartItemDTO(Product product, Integer quantity) {
        ProductDTO productDTO = productService.toProductDTO(product);
        return ProductCartItemDTO.builder()
                .product(productDTO)
                .quantity(quantity)
                .build();
    }

    @Override
    public CartItemDTO toCartItemDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .products(cartItem.getCartItemProducts().stream()
                        .map(cartItemProduct -> ProductCartItemDTO.builder()
                                .product(productService.toProductDTO(cartItemProduct.getProduct()))
                                .quantity(cartItemProduct.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
