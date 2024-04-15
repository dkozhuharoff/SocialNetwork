package com.social.network.controller;

import com.social.network.dto.PostDto;
import com.social.network.model.Post;
import com.social.network.model.User;
import com.social.network.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.social.network.utils.constants.Paths.*;

@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Fetch posts for category",
            description = "Fetch all posts for certain category by providing category id.")
    @GetMapping(MAIN_CATEGORYID_POST_PATH)
    public ResponseEntity getPostsByCategoryId(@PathVariable long categoryId) {
        List<PostDto> postList = postService.fetchPostsByCategoryId(categoryId);

        return new ResponseEntity(postList, HttpStatus.OK);
    }

    @Operation(summary = "Fetch post from category",
            description = "Fetch a post for certain category by providing category id.")
    @GetMapping(MAIN_CATEGORYID_POST_PATH + "/{postId}")
    public ResponseEntity getPostByCategoryId(@PathVariable long categoryId, @PathVariable long postId) {
        PostDto postDto = postService.fetchPostByCategoryId(categoryId, postId);

        return new ResponseEntity(postDto, HttpStatus.OK);
    }

    @Operation(summary = "Create post in category",
            description = "Create a post for certain category by providing category id.")
    @PostMapping(MAIN_CATEGORYID_POST_PATH)
    public ResponseEntity createPost(@AuthenticationPrincipal User user, @RequestBody Post post, @PathVariable long categoryId) {
        PostDto postDto = postService.createPost(user, post, categoryId);

        return new ResponseEntity(postDto, HttpStatus.OK);
    }

    @Operation(summary = "Update post",
            description = "Edit an existing post by providing post id.")
    @PutMapping(MAIN_POST_PATH + "/{postId}")
    public ResponseEntity updatePost(@AuthenticationPrincipal User user, @PathVariable long postId, @RequestBody Post post) {
        PostDto postDto = postService.updatePost(user, postId, post);

        return new ResponseEntity(postDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete post",
            description = "Delete an existing post by providing post id.")
    @DeleteMapping(MAIN_POST_PATH + "/{postId}")
    public ResponseEntity deletePost(@AuthenticationPrincipal User user, @PathVariable long postId) {
        postService.deletePost(user, postId);
        String message = "Post deleted successfully";

        return new ResponseEntity(message, HttpStatus.OK);
    }
}
