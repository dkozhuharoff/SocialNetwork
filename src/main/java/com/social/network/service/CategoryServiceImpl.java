package com.social.network.service;

import com.social.network.exception.NotFoundException;
import com.social.network.repository.CategoryRepository;
import com.social.network.repository.PostRepository;
import com.social.network.model.Post;
import com.social.network.utils.Common;
import com.social.network.utils.Mapper;
import com.social.network.utils.constants.ErrorMessages;
import com.social.network.dto.CategoryDto;
import com.social.network.model.Category;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private Mapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository, ModelMapper modelMapper, Mapper mapper) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.mapper = mapper;
    }

    public List<CategoryDto> fetchCategories() {
        try {
            List<Category> categoryList = categoryRepository.findAll();
            if (Common.isNullOrEmpty(categoryList)) {
                categoryList = Collections.emptyList();
            }

            List<CategoryDto> categoryDtoList = mapper.mapCollection(categoryList, CategoryDto.class);
            return categoryDtoList;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.RUNTIME_ERROR);
        }
    }

    public CategoryDto findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        return categoryDto;
    }

    public CategoryDto createCategory(Category category) {
        try {
            Category newCategory = categoryRepository.save(category);
            CategoryDto categoryDto = modelMapper.map(newCategory, CategoryDto.class);

            return categoryDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.CATEGORY_NOT_CREATED);
        }
    }

    public CategoryDto updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));
        try {
            existingCategory.setName(category.getName());
            Category updatedCategory = categoryRepository.save(existingCategory);
            CategoryDto categoryDto = modelMapper.map(updatedCategory, CategoryDto.class);

            return categoryDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.CATEGORY_NOT_UPDATED);
        }
    }

    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));
        try {
            deleteCategoryForPost(category);
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.CATEGORY_NOT_DELETED);
        }
    }

    private void deleteCategoryForPost(Category category) {
        List<Post> postList = category.getPosts();
        if (!Common.isNullOrEmpty(postList)) {
            for (Post post : postList) {
                List<Category> categoriesOfPost = post.getCategories();
                if (!Common.isNullOrEmpty(categoriesOfPost) && categoriesOfPost.size() > 1) {
                    categoriesOfPost.remove(category);
                    post.setCategories(categoriesOfPost);
                    postRepository.save(post);
                } else {
                    postRepository.deleteById(post.getId());
                }
            }
        }
    }

}
