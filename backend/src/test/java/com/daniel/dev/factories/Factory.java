package com.daniel.dev.factories;

import com.daniel.dev.dto.ProductDTO;
import com.daniel.dev.entities.Category;
import com.daniel.dev.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Nice phone", 800.0, "https://australopitecus", Instant.now());
        product.getCategories().add(createCategory());
        return product;
    }

    public static Category createCategory(){
       return new Category(1L, "Livros");
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
