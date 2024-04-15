package com.social.network.controller;

import com.social.network.dto.CategoryDto;
import com.social.network.model.Category;
import com.social.network.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.social.network.utils.constants.Paths.MAIN_CATEGORY_PATH;

@RestController
@RequestMapping(MAIN_CATEGORY_PATH)
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Fetch all categories",
            description = "This endpoint returns list of the existing categories.")
    @GetMapping
    public ResponseEntity getCategories() {
        List<CategoryDto> categoryList = categoryService.fetchCategories();

        return new ResponseEntity(categoryList, HttpStatus.OK);
    }

    @Operation(summary = "Fetch category by providing id",
            description = "This endpoint returns exactly one category. You need to provide id of the category.")
    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable long id) {
        CategoryDto category = categoryService.findCategoryById(id);

        return new ResponseEntity(category, HttpStatus.OK);
    }

    @Operation(summary = "Create new category",
            description = "Create a category. You need to provide name in request body.")
    @PostMapping
    public ResponseEntity createCategory(@RequestBody @Valid Category category) {
        CategoryDto categoryDto = categoryService.createCategory(category);

        return new ResponseEntity(categoryDto, HttpStatus.OK);
    }

    @Operation(summary = "Update existing category",
            description = "Edit an existing category. Provide new name for category and the id of the category you want to edit.")
    @PutMapping("/{id}")
    public ResponseEntity updateCategoryById(@PathVariable long id, @RequestBody @Valid Category category) {
        CategoryDto categoryDto = categoryService.updateCategory(id, category);

        return new ResponseEntity(categoryDto, HttpStatus.OK);
    }


    @Operation(summary = "Delete existing category",
            description = "Delete existing category by providing the id of category you want to delete.")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategoryById(@PathVariable long id) {
        categoryService.deleteCategoryById(id);
        String message = "Category deleted successfully";

        return new ResponseEntity(message, HttpStatus.OK);
    }
}
