package klx.tech.community.workshop.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Only include in specific attributes
@Table(name = "product")
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

}
