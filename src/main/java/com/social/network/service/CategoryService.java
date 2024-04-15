package com.social.network.service;

import com.social.network.dto.CategoryDto;
import com.social.network.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto findCategoryById(Long id);
    List<CategoryDto> fetchCategories();
    CategoryDto createCategory(Category category);
    CategoryDto updateCategory(Long id, Category category);
    void deleteCategoryById(Long id);
}