package klx.tech.community.workshop.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ProductKey implements Serializable {

    @OneToOne
    private Product product; // Foreign key to Product entity
}

