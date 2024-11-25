package klx.tech.community.workshop.controllers;

import klx.tech.community.workshop.dto.CartItemDTO;
import klx.tech.community.workshop.dto.CartItemRequestDTO;
import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.dto.ProductDTO;
import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.CartItemProduct;
import klx.tech.community.workshop.entities.Product;
import klx.tech.community.workshop.services.CartItemService;
import klx.tech.community.workshop.services.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart-items")
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemService cartItemService;
    private final ProductService productService;

    public CartItemController(CartItemService cartItemService, ProductService productService) {
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    /**
     * Finds a CartItem by its ID and returns it as a DTO.
     *
     * @param id The ID of the CartItem.
     * @return ResponseEntity containing the CartItemDTO or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductCartItemDTO> getProductCartItem(@PathVariable Long id) {
        Optional<CartItem> cartItemOptional = cartItemService.findById(id);
    
        if (cartItemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        CartItem cartItem = cartItemOptional.get();
        Product product = cartItem.getCartItemProducts().iterator().next().getProduct(); // Example: Single product
        Integer quantity = cartItem.getCartItemProducts().iterator().next().getQuantity();
    
        ProductCartItemDTO productCartItemDTO = cartItemService.toProductCartItemDTO(product, quantity);
        return ResponseEntity.ok(productCartItemDTO);
    }
    
    /**
     * Creates a new CartItem based on the request body.
     *
     * @param request The CartItemRequest DTO containing product IDs and quantities.
     * @return ResponseEntity containing the created CartItemDTO or a validation error message.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CartItemRequestDTO request) {
        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input: No products provided.");
        }

        // Create the CartItem
        CartItem cartItem = new CartItem();

        // Map product IDs and quantities into CartItemProducts
        Set<CartItemProduct> cartItemProducts = request.getProducts().entrySet().stream().map(entry -> {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            return productService.findById(productId).map(product -> {
                CartItemProduct cartItemProduct = new CartItemProduct();
                cartItemProduct.setProduct(product);
                cartItemProduct.setQuantity(quantity);
                cartItemProduct.setCartItem(cartItem);
                return cartItemProduct;
            }).orElseThrow(() -> new RuntimeException("Product not found for productId: " + productId));
        }).collect(Collectors.toSet());

        cartItem.setCartItemProducts(cartItemProducts);

        // Save the CartItem
        CartItem savedCartItem = cartItemService.create(cartItem);

        return ResponseEntity.ok(toCartItemDTO(savedCartItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CartItemRequestDTO request) {
        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input: No products provided.");
        }

        try {
            // Actualizar el CartItem
            CartItem updatedCartItem = cartItemService.update(id, request.getProducts());
            return ResponseEntity.ok(toCartItemDTO(updatedCartItem));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            cartItemService.delete(id); // Llama al servicio para eliminar el CartItem
            return ResponseEntity.noContent().build(); // Respuesta HTTP 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Manejo de errores
        }
    }

    /**
     * Converts a CartItem entity into a CartItemDTO.
     *
     * @param cartItem The CartItem entity to convert.
     * @return The corresponding CartItemDTO.
     */
    private CartItemDTO toCartItemDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .products(cartItem.getCartItemProducts().stream()
                    .map(cartItemProduct -> ProductCartItemDTO.builder()
                        .product(toProductDTO(cartItemProduct.getProduct())) // Convert Product to ProductDTO
                        .quantity(cartItemProduct.getQuantity()) // Set the quantity
                        .build()
                    )
                    .collect(Collectors.toList())) // Collect into List<ProductCartItemDTO>
                .build();
    }

    private ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .imageBase64(product.getImageUrl()) // Assumes Base64 string or path
            .discount(product.getDiscount())
            .favorite(product.getFavorite())
            .build();
    }
}
