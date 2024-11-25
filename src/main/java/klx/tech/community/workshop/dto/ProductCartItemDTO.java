package klx.tech.community.workshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCartItemDTO {

    private ProductDTO product; // The product details
    private Integer quantity;   // The quantity in the CartItem
}
