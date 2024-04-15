package com.social.network.service;

import com.social.network.dto.CategoryDto;
import com.social.network.dto.PostDto;
import com.social.network.enums.UserRole;
import com.social.network.exception.NotFoundException;
import com.social.network.model.Category;
import com.social.network.model.Post;
import com.social.network.model.User;
import com.social.network.repository.PostRepository;
import com.social.network.utils.Mapper;
import com.social.network.utils.constants.ErrorMessages;
import com.social.network.utils.Common;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final UserServiceImpl userService;
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final Mapper mapper;

    public PostServiceImpl(UserServiceImpl userService, PostRepository postRepository, CategoryService categoryService, ModelMapper modelMapper, Mapper mapper) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.mapper = mapper;
    }

    public PostDto createPost(User user, Post post, Long categoryId) {
        CategoryDto category = categoryService.findCategoryById(categoryId);
        if (Common.isNullOrEmpty(category)) {
            throw new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
        }

        try {
            post.setUser(user);
            Category updatedCategory = modelMapper.map(category, Category.class);
            List<Category> categoryList = new ArrayList<>();
            categoryList.add(updatedCategory);
            post.setCategories(categoryList);
            Post newPost = postRepository.save(post);
            PostDto postDto = modelMapper.map(newPost, PostDto.class);

            List<Post> posts = updatedCategory.getPosts();
            if (posts == null) {
                posts = new ArrayList<>();
                posts.add(newPost);
                updatedCategory.setPosts(posts);
            } else {
                updatedCategory.getPosts().add(newPost);
                updatedCategory.setPosts(updatedCategory.getPosts());
            }
            categoryService.updateCategory(categoryId, updatedCategory);

            List<Post> userPosts = user.getPosts();
            userPosts.add(newPost);
            user.setPosts(userPosts);
            userService.updateUser(user.getId(), user);

            return postDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.POST_NOT_CREATED);
        }
    }

    public List<PostDto> fetchPostsByCategoryId(Long categoryId) {
        CategoryDto category = categoryService.findCategoryById(categoryId);
        if (category == null) {
            throw new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
        }

        List<Post> postList = category.getPosts();
        if (postList == null) {
            throw new RuntimeException(ErrorMessages.FETCHING_POST_LIST_FAILED);
        }

        List<PostDto> postDtoList;
        try {
            postDtoList = mapper.mapCollection(postList, PostDto.class);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.FETCHING_POST_LIST_FAILED);
        }

        return postDtoList;
    }

    public PostDto fetchPostByCategoryId(Long categoryId, Long postId) {
        CategoryDto category = categoryService.findCategoryById(categoryId);
        if (Common.isNullOrEmpty(category)) {
            throw new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
        }

        try {
            List<Post> postList = category.getPosts();
            if (postList == null) {
                throw new RuntimeException(ErrorMessages.FETCHING_POST_LIST_FAILED);
            }

            Post post = postList
                    .stream()
                    .filter(postEntry -> postEntry.getId() == postId)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ErrorMessages.POST_NOT_FOUND));
            PostDto postDto = modelMapper.map(post, PostDto.class);

            return postDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.RUNTIME_ERROR);
        }
    }

    public PostDto fetchPostById(Long postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.POST_NOT_FOUND));
        PostDto postDto = modelMapper.map(existingPost, PostDto.class);

        return postDto;
    }

    public PostDto updatePost(User user, Long postId, Post post) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.POST_NOT_FOUND));

        if (user.getRole() == null || user.getRole().getValue().equals(UserRole.USER.toString())) {
            Common.isUserAuthorizedToExecuteThisAction(user.getId(), existingPost.getUser().getId());
        }
        try {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            Post updatedPost = postRepository.save(existingPost);
            PostDto postDto = modelMapper.map(updatedPost, PostDto.class);

            return postDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.POST_NOT_UPDATED);
        }
    }

    public void deletePost(User user, Long postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.POST_NOT_FOUND));

        if (user.getRole() == null || user.getRole().getValue().equals(UserRole.USER.toString())) {
            Common.isUserAuthorizedToExecuteThisAction(user.getId(), existingPost.getUser().getId());
        }
        try {
            postRepository.deleteById(postId);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.POST_NOT_DELETED);
        }
    }
}