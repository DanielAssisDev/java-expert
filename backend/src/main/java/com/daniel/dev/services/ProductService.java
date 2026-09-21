package com.daniel.dev.services;

import com.daniel.dev.dto.CategoryDTO;
import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.entities.Category;
import com.daniel.dev.entities.Product;
import com.daniel.dev.projections.ProductProjection;
import com.daniel.dev.repositories.CategoryRepository;
import com.daniel.dev.repositories.ProductRepository;
import com.daniel.dev.services.exceptions.DatabaseException;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import com.daniel.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable, String name, String categories) {
        List<Long> categoriesLong = List.of();
        if (!"0".equals(categories)) {
            categoriesLong = Arrays.stream(categories.split(",")).map(Long::parseLong).toList();
        }
        Page<ProductProjection> page = productRepository.searchProducts(pageable, name.trim(), categoriesLong);
        List<Long> productsLong = page.map(ProductProjection::getId).stream().toList();
        List<Product> entities = productRepository.searchProductsWithCategories(productsLong);
        entities = (List<Product>) Utils.replace(page.getContent(), entities);
        return new PageImpl<>(
                entities.stream().map(p -> new ProductDTO(p, p.getCategories())).toList(),
                page.getPageable(),
                page.getTotalElements());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.getProductById(id);
        return new ProductDTO(product.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado")), product.get().getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product();
        copyDTOToEntity(productDTO, product);
        return new ProductDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        Product product = productRepository.getReferenceById(id);
        copyDTOToEntity(productDTO, product);
        return new ProductDTO(productRepository.save(product));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação da integridade referencial");
        }
    }

    public void copyDTOToEntity(ProductDTO productDTO, Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImgUrl(productDTO.getImgUrl());
        product.setPrice(productDTO.getPrice());
        product.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            product.getCategories().add(category);
        }
    }
}
