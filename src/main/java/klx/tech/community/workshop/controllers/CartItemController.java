package klx.tech.community.workshop.controllers;

import klx.tech.community.workshop.dto.CartItemDTO;
import klx.tech.community.workshop.dto.CartItemRequestDTO;
import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.services.CartItemService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCartItemDTO> findById(@PathVariable Long id) {
        try {
            ProductCartItemDTO productCartItemDTO = cartItemService.findById(id);
            return ResponseEntity.ok(productCartItemDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<ProductCartItemDTO> findAll() {
        return cartItemService.findAll();
    }

    @PostMapping
    public ResponseEntity<CartItemDTO> create(@RequestBody CartItemRequestDTO request) {
        try {
            CartItemDTO cartItemDTO = cartItemService.toCartItemDTO(cartItemService.create(request));
            return ResponseEntity.ok(cartItemDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDTO> update(@PathVariable Long id, @RequestBody CartItemRequestDTO request) {
        try {
            CartItemDTO updatedCartItemDTO = cartItemService.toCartItemDTO(cartItemService.update(id, request));
            return ResponseEntity.ok(updatedCartItemDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            cartItemService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
