package klx.tech.community.workshop.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Boolean favorite;
    private String imageUrl; // Base64 image
}
