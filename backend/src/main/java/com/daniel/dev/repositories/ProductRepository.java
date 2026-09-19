package com.daniel.dev.repositories;

import com.daniel.dev.entities.Product;
import com.daniel.dev.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.categories WHERE p.id = :id",
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.categories")
    Optional<Product> getProductById(Long id);

    @Query(nativeQuery = true, value = """
            SELECT DISTINCT p.id, p.name FROM tb_product p
            INNER JOIN tb_product_category pc ON p.id=pc.product_id
            WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name ,'%')) ORDER BY p.name
            """, countQuery = """
            SELECT COUNT(*) FROM(
            SELECT DISTINCT p.id, p.name FROM tb_product p
            INNER JOIN tb_product_category pc ON p.id=pc.product_id
            WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name ,'%'))
            ) AS tb_result
            """)
    Page<ProductProjection> searchProducts(Pageable pageable, String name, List<Long> categoryIds);

}
