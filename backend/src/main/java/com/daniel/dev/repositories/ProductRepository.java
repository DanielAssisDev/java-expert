package com.daniel.dev.repositories;

import com.daniel.dev.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.categories WHERE p.id = :id",
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.categories")
    Optional<Product> getProductById(Long id);

    @Query(value = "SELECT p FROM Product p JOIN FETCH p.categories",
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.categories")
    Page<Product> getProducts(Pageable pageable);
}
