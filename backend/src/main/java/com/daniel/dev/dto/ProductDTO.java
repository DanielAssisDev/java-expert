package com.daniel.dev.dto;

import com.daniel.dev.entities.Category;
import com.daniel.dev.entities.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductDTO {
    private Long id;
    @Size(message = "O nome deve ter de 3 a 80 caracteres", min = 3, max = 80)
    @NotBlank(message = "O nome não pode estar vazio")
    private String name;
    @Column(columnDefinition = "TEXT")
    @Size(min = 10, message = "A descrição precisa ter no mínimo 10 caracteres")
    @NotBlank(message = "A descrição não pode estar vazia")
    private String description;
    @NotNull(message = "Campo requerido")
    @Positive(message = "O preço deve ter valor positivo")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "A data do produto não pode ser futura")
    private Instant date;
    @NotEmpty(message = "O produto deve ter ao menos uma categoria")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
        date = product.getDate();
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(x -> this.categories.add(new CategoryDTO(x)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
