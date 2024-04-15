package com.social.network.service;

import com.social.network.dto.CommentDto;
import com.social.network.dto.PostDto;
import com.social.network.enums.UserRole;
import com.social.network.exception.NotFoundException;
import com.social.network.model.Comment;
import com.social.network.model.Post;
import com.social.network.model.User;
import com.social.network.utils.Mapper;
import com.social.network.utils.constants.ErrorMessages;
import com.social.network.repository.CommentRepository;
import com.social.network.utils.Common;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostService postService;
    private final UserServiceImpl userService;
    private CommentRepository commentRepository;
    private ModelMapper modelMapper;
    private Mapper mapper;

    public CommentServiceImpl(PostService postService, UserServiceImpl userService, CommentRepository commentRepository, ModelMapper modelMapper, Mapper mapper) {
        this.postService = postService;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.mapper = mapper;
    }

    public List<CommentDto> fetchCommentsByPostId(Long postId) {
        PostDto post = postService.fetchPostById(postId);
        if (Common.isNullOrEmpty(post)) {
            throw new NotFoundException(ErrorMessages.COMMENT_NOT_FOUND);
        }

        try {
            List<Comment> commentList = post.getUser().getComments();
            if (commentList == null) {
                throw new RuntimeException(ErrorMessages.FETCHING_COMMENT_LIST_FAILED);
            }

            List<CommentDto> commentDtoList = mapper.mapCollection(commentList, CommentDto.class);
            return commentDtoList;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.RUNTIME_ERROR);
        }
    }

    public CommentDto createComment(User user, Long postId, Comment comment) {
        PostDto post = postService.fetchPostById(postId);
        if (Common.isNullOrEmpty(post)) {
            throw new NotFoundException(ErrorMessages.COMMENT_NOT_FOUND);
        }

        try {
            comment.setUser(user);
            Post updatedPost = modelMapper.map(post, Post.class);
            comment.setPost(updatedPost);
            Comment newComment = commentRepository.save(comment);
            CommentDto commentDto = modelMapper.map(newComment, CommentDto.class);

            List<Comment> comments = updatedPost.getComments();
            if (comments == null) {
                comments = new ArrayList<>();
                comments.add(newComment);
                updatedPost.setComments(comments);
            } else {
                updatedPost.getComments().add(newComment);
                updatedPost.setComments(updatedPost.getComments());
            }
            postService.updatePost(updatedPost.getUser(), postId, updatedPost);

            List<Comment> userComments = user.getComments();
            userComments.add(newComment);
            user.setComments(userComments);
            userService.updateUser(user.getId(), user);


            return commentDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.COMMENT_NOT_CREATED);
        }
    }

    public CommentDto findCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.COMMENT_NOT_FOUND));

        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);

        return commentDto;
    }

    public CommentDto updateComment(User user, Long commentId, Comment comment) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException((ErrorMessages.COMMENT_NOT_FOUND)));

        if (user.getRole() == null || user.getRole().getValue().equals(UserRole.USER.toString())) {
            Common.isUserAuthorizedToExecuteThisAction(user.getId(), existingComment.getUser().getId());
        }
        try {
            existingComment.setContent(comment.getContent());
            Comment updatedComment = commentRepository.save(existingComment);
            CommentDto commentDto = modelMapper.map(updatedComment, CommentDto.class);

            return commentDto;
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.COMMENT_NOT_UPDATED);
        }
    }

    public void deleteComment(User user, Long commentId) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException((ErrorMessages.COMMENT_NOT_FOUND)));

        if (user.getRole() == null || user.getRole().getValue().equals(UserRole.USER.toString())) {
            Common.isUserAuthorizedToExecuteThisAction(user.getId(), existingComment.getUser().getId());
        }
        try {
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.COMMENT_NOT_DELETED);
        }
    }
}
