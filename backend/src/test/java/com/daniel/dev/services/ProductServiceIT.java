package com.daniel.dev.services;

import com.daniel.dev.repositories.ProductRepository;
import com.daniel.dev.services.exceptions.DatabaseException;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    private Long id;
    private Long certainlyNonExistingId;
    private Long totalProducts;
    private Pageable page;


    @BeforeEach
    void setUp() throws Exception {
        id = 1L;
        certainlyNonExistingId = 1000L;
        totalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productService.delete(id);
        Assertions.assertEquals(productRepository.count(), totalProducts-1);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(certainlyNonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseException() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(certainlyNonExistingId);
        });
    }
}
