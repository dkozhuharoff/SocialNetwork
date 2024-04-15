package com.social.network.service;

import com.social.network.dto.CommentDto;
import com.social.network.model.Comment;
import com.social.network.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<CommentDto> fetchCommentsByPostId(Long postId);
    CommentDto findCommentById(Long id);
    CommentDto createComment(User user, Long postId, Comment comment);
    CommentDto updateComment(User user, Long commentId, Comment comment);
    void deleteComment(User user, Long commentId);
}
