package klx.tech.community.workshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemProductDTO {

    private Long id; // Unique identifier for the CartItemProduct
    private Long productId; // ID of the associated Product
    private Integer quantity; // Quantity of the Product in the CartItem
}