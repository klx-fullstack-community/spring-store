package klx.tech.community.workshop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor // Constructor sin argumentos (Lombok)
@AllArgsConstructor // Constructor con todos los argumentos (Lombok)
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Only include in specific attributes
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Include id en hashCode y equals
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = true)
    private Double discount;

    @Column(nullable = false)
    private Boolean favorite;

    public Product(String name, String description, Double price, String imageUrl, Double discount, Boolean favorite) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discount = discount;
        this.favorite = favorite;
    }

}
