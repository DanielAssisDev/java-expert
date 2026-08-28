package com.daniel.dev.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        Long id = 1L;
        productRepository.deleteById(id);
        Assertions.assertFalse(productRepository.findById(id).isPresent());
    }
}
