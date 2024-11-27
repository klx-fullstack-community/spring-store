package klx.tech.community.workshop.services;

import java.util.List;

import klx.tech.community.workshop.dto.CartItemDTO;
import klx.tech.community.workshop.dto.CartItemRequestDTO;
import klx.tech.community.workshop.dto.ProductCartItemDTO;
import klx.tech.community.workshop.entities.CartItem;

public interface CartItemService {

    ProductCartItemDTO findById(Long id);

    List<ProductCartItemDTO> findAll();

    CartItem create(CartItemRequestDTO request);

    CartItem update(Long id, CartItemRequestDTO request);

    void delete(Long id);

    CartItemDTO toCartItemDTO(CartItem cartItem);
}
