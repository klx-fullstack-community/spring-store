package klx.tech.community.workshop.services;

import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.entities.ProductKey;
import klx.tech.community.workshop.repositories.CartItemRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private static final String IMAGE_DIRECTORY = "images"; // Directory for image files

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public List<ProductCartItemDTO> addProductToCartItem(ProductDTO productDTO) {
        // Try to fetch the product from the database
        Product product = productService.findById(productDTO.getId()).orElse(null);
    
        // If the product does not exist, return the current list without making changes
        if (product == null) {
            return findAll(); // Return the current state of the cart
        }
    
        // Create a composite key
        ProductKey productKey = new ProductKey();
        productKey.setProduct(product);
    
        // Find or create the CartItem for the product
        CartItem cartItem = cartItemRepository.findById(productKey).orElseGet(() -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setId(productKey);
            newCartItem.setQuantity(0);
            return newCartItem;
        });
    
        // Increment the quantity
        cartItem.setQuantity(cartItem.getQuantity() + 1);
    
        // Save the updated CartItem
        cartItemRepository.save(cartItem);
    
        // Return the updated list of ProductCartItemDTO
        return findAll();
    }

    @Override
    @Transactional
    public List<ProductCartItemDTO> deleteProductFromCartItem(Long productId) {
        // Try to fetch the product from the database
        Product product = productService.findById(productId).orElse(null);
    
        // If the product does not exist, return the current list without making changes
        if (product == null) {
            return findAll(); // Return the current state of the cart
        }
    
        // Create a composite key
        ProductKey productKey = new ProductKey();
        productKey.setProduct(product);
    
        // Find the CartItem associated with the product
        CartItem cartItem = cartItemRepository.findById(productKey).orElse(null);
    
        if (cartItem != null) {
            if (cartItem.getQuantity() > 1) {
                // Decrease the quantity
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemRepository.save(cartItem);
            } else {
                // Remove the CartItem if the quantity becomes zero
                cartItemRepository.delete(cartItem);
            }
        }
    
        // Return the updated list of ProductCartItemDTO
        return findAll();
    }
    

    @Override
    @Transactional
    public List<ProductCartItemDTO> findAll() {
        // Fetch all CartItems from the database
        List<CartItem> cartItems = cartItemRepository.findAll();
    
        // Transform each CartItem into a ProductCartItemDTO
        return cartItems.stream()
                .map(cartItem -> ProductCartItemDTO.builder()
                        .product(productService.toProductDTO(cartItem.getId().getProduct())) // Access the product via the composite key
                        .quantity(cartItem.getQuantity()) // Get the quantity
                        .build())
                .collect(Collectors.toList());
    }

}
