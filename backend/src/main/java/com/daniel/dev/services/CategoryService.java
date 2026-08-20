package com.daniel.dev.services;

import com.daniel.dev.dto.CategoryDTO;
import com.daniel.dev.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll(){
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }
}
