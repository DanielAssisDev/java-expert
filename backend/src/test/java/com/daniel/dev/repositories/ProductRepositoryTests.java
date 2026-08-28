package com.daniel.dev.repositories;

import com.daniel.dev.entities.Product;
import com.daniel.dev.factories.Factory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    private Long id;
    private Long certainlyNotExistentId = 10000L;
    private Long totalProducts;

    @BeforeEach
    void setUp() {
        id = 1L;
        totalProducts = productRepository.count();
    }

    @AfterEach
    void tearDown() {}

    @BeforeAll
    static void beforeAll() {}

    @AfterAll
    static void afterAll() {}

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        productRepository.deleteById(id);
        Assertions.assertFalse(productRepository.findById(id).isPresent());
    }

    @Test
    public void saveShouldCreateNewObjectEvenWhenIdIsNullAndWithAutoIncrement(){
        Product product = Factory.createProduct();
        product.setId(null);
        product = productRepository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(product.getId(), totalProducts+1);
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalObjectWhenIdExists(){
        Optional<Product> optional = productRepository.findById(id);
        Assertions.assertFalse(optional.isEmpty());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalObjectWhenIdDoesNotExist(){
        Optional<Product> optional = productRepository.findById(certainlyNotExistentId);
        Assertions.assertTrue(optional.isEmpty());
    }
}
