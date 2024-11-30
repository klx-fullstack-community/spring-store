package klx.tech.community.workshop.repositories;

import klx.tech.community.workshop.entities.CartItem;
import klx.tech.community.workshop.entities.ProductKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, ProductKey> {}
    

