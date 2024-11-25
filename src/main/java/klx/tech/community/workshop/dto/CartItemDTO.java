package klx.tech.community.workshop.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class CartItemDTO {

    private Long id; 
    private List<ProductCartItemDTO> products; 
}
