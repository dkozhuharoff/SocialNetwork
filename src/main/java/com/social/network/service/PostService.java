package com.social.network.service;

import com.social.network.dto.PostDto;
import com.social.network.model.Post;
import com.social.network.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    PostDto createPost(User user, Post post, Long categoryId);
    List<PostDto> fetchPostsByCategoryId(Long categoryId);
    PostDto fetchPostByCategoryId(Long categoryId, Long postId);
    PostDto fetchPostById(Long postId);
    PostDto updatePost(User user, Long postId, Post post);
    void deletePost(User user, Long postId);
}