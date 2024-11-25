package klx.tech.community.workshop.dto;

import java.util.Map;

public class CartItemRequestDTO {

    private Map<Long, Integer> products; // Map of productId to quantity

    // Getters and Setters
    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }
    
}
