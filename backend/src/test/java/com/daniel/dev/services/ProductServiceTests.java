package com.daniel.dev.services;

import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.entities.Category;
import com.daniel.dev.entities.Product;
import com.daniel.dev.factories.Factory;
import com.daniel.dev.repositories.CategoryRepository;
import com.daniel.dev.repositories.ProductRepository;
import com.daniel.dev.services.exceptions.DatabaseException;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTests {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Long id;
    private Long certainlyNotExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        id = 25L;
        certainlyNotExistingId = 10000L;
        dependentId = 2L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of());

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.existsById(id)).thenReturn(true);
        Mockito.when(productRepository.existsById(certainlyNotExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(productRepository.getProductById(id)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.getProductById(certainlyNotExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(productRepository.getReferenceById(id)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(certainlyNotExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(categoryRepository.getReferenceById(id)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(certainlyNotExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
                productService.delete(id);
        });
        Mockito.verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(certainlyNotExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }

    @Test
    public void findAllPaged(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAll(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getProductIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO productDTO = productService.findById(id);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void getProductIdShouldThrowResourceNotFoundExceptionWhenIdIsInvalid(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(certainlyNotExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){
        ProductDTO productDTO = productService.update(id, this.productDTO);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdIsInvalid(){
        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            productService.update(certainlyNotExistingId, this.productDTO);
        });
    }
}
