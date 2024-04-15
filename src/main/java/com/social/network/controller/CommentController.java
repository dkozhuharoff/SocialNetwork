package com.social.network.controller;

import com.social.network.dto.CommentDto;
import com.social.network.model.Comment;
import com.social.network.model.User;
import com.social.network.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.social.network.utils.constants.Paths.*;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Fetch comments",
            description = "Fetch all comments by providing post id.")
    @GetMapping(MAIN_POSTID_COMMENT_PATH)
    public ResponseEntity getComments(@PathVariable long postId) {
        List<CommentDto> commentList = commentService.fetchCommentsByPostId(postId);

        return new ResponseEntity(commentList, HttpStatus.OK);
    }

    @Operation(summary = "Create a comment",
            description = "Add a comment to post by providing post id.")
    @PostMapping(MAIN_POSTID_COMMENT_PATH)
    public ResponseEntity createComment(@AuthenticationPrincipal User user, @RequestBody Comment comment, @PathVariable long postId) {
        CommentDto commentDto  = commentService.createComment(user, postId, comment);

        return new ResponseEntity(commentDto, HttpStatus.OK);
    }

    @Operation(summary = "Update a comment",
            description = "Edit an existing comment by providing comment id.")
    @PutMapping(MAIN_COMMENT_PATH + "/{commentId}")
    public ResponseEntity updateComment(@AuthenticationPrincipal User user, @PathVariable long commentId, @RequestBody Comment comment) {
        CommentDto commentDto = commentService.updateComment(user, commentId, comment);

        return new ResponseEntity(commentDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete a comment",
            description = "Delete an existing comment by providing comment id.")
    @DeleteMapping(MAIN_COMMENT_PATH + "/{commentId}")
    public ResponseEntity deleteComment(@AuthenticationPrincipal User user, @PathVariable long commentId) {
        commentService.deleteComment(user, commentId);
        String message = "Comment deleted successfully";

        return new ResponseEntity(message, HttpStatus.OK);
    }
}
