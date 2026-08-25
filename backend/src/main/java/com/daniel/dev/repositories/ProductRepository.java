package com.daniel.dev.repositories;

import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = """
            SELECT p FROM Product p JOIN FETCH p.categories      
            WHERE p.id = :id
            """)
    Optional<Product> getProductById(Long id);
}
