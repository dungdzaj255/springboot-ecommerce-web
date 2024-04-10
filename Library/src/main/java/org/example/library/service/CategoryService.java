package org.example.library.service;

import org.example.library.dto.CategoryDto;
import org.example.library.model.Category;

import java.util.List;

public interface CategoryService {
    /*Admin*/
    List<Category> findAll();
    Category save(Category category);
    Category findById(Long id);
    Category update(Category category);
    void deleteById(Long id);
    void enableById(Long id);
    List<Category> findAllByActivated();


    /*Customer*/
    List<CategoryDto> getCategoryAndProduct();
}
