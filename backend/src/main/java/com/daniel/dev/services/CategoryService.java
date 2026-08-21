package com.daniel.dev.services;

import com.daniel.dev.dto.CategoryDTO;
import com.daniel.dev.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<CategoryDTO> findAll(Pageable pageable){
        return categoryRepository.findAll(pageable).map(CategoryDTO::new);
    }
}
