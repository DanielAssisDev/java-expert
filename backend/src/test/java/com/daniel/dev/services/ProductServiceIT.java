package com.daniel.dev.services;

import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.repositories.ProductRepository;
import com.daniel.dev.services.exceptions.DatabaseException;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    private Long id;
    private Long certainlyNonExistingId;
    private Long dependentId;
    private Long totalProducts;
    private Pageable page;


    @BeforeEach
    void setUp() throws Exception {
        id = 1L;
        certainlyNonExistingId = 1000L;
        dependentId = 2L;
        totalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productService.delete(id);
        Assertions.assertEquals(productRepository.count(), totalProducts - 1);
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
            productService.delete(dependentId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {
        Page<ProductDTO> result = productService.findAll(PageRequest.of(0, 10));
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(totalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        Page<ProductDTO> result = productService.findAll(PageRequest.of(50, 10));
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() {
        Page<ProductDTO> result = productService.findAll(PageRequest.of(0, 10, Sort.by("name")));
        Assertions.assertEquals("Macbook Pro", result.getContent().getFirst().getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}
