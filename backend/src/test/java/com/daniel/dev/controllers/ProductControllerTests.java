package com.daniel.dev.controllers;

import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.factories.Factory;
import com.daniel.dev.services.ProductService;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private Long id;
    private Long certainlyNotExistingId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;


    @BeforeEach
    void setUp() throws Exception{
        id = 1L;
        certainlyNotExistingId = 1000L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        when(productService.findAll(ArgumentMatchers.any())).thenReturn(page);
        when(productService.findById(id)).thenReturn(productDTO);
        when(productService.findById(certainlyNotExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findByIdShouldThrowResourseNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/{id}", certainlyNotExistingId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andExpect(jsonPath("$.id").doesNotExist());
    }

}
