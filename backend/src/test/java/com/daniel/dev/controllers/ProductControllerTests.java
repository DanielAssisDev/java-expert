package com.daniel.dev.controllers;

import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.factories.Factory;
import com.daniel.dev.services.ProductService;
import com.daniel.dev.services.exceptions.DatabaseException;
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
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long id;
    private Long certainlyNonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        id = 1L;
        certainlyNonExistingId = 1000L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        when(productService.findAll(ArgumentMatchers.any())).thenReturn(page);
        when(productService.findById(id)).thenReturn(productDTO);
        when(productService.findById(certainlyNonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(productService.update(id, productDTO)).thenReturn(productDTO);
        when(productService.update(certainlyNonExistingId, productDTO)).thenThrow(ResourceNotFoundException.class);
        doNothing().when(productService).delete(id);
        doThrow(ResourceNotFoundException.class).when(productService).delete(certainlyNonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);
        when(productService.insert(productDTO)).thenReturn(productDTO);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .exists()).andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void findByIdShouldThrowResourseNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/{id}", certainlyNonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(put("/products/{id}", id)
                        .content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Phone"));
    }

    @Test
    public void updateShouldThrowResourseNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(put("/products/{id}", certainlyNonExistingId)
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldCreateNewProduct() throws Exception {
        mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnResourceNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/products/{id}", certainlyNonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
