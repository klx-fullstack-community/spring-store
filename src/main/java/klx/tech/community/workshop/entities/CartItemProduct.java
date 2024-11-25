package klx.tech.community.workshop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_item_product")
public class CartItemProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Unique primary key
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_item_id", nullable = false) // Reference to CartItem
    private CartItem cartItem;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Reference to Product
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // Quantity associated with the product
    
}
