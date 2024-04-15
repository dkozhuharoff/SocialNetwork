package com.social.network.service;

import com.social.network.exception.InvalidJwtException;
import com.social.network.exception.NotFoundException;
import com.social.network.repository.PostRepository;
import com.social.network.repository.UserRepository;
import com.social.network.dto.UserDto;
import com.social.network.model.Post;
import com.social.network.repository.CommentRepository;
import com.social.network.utils.Common;
import com.social.network.utils.Mapper;
import com.social.network.utils.constants.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.social.network.dto.SignUpDto;
import com.social.network.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {
  private UserRepository userRepository;
  private PostRepository postRepository;
  private CommentRepository commentRepository;
  private ModelMapper modelMapper;
  private Mapper mapper;

  public UserServiceImpl(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper, Mapper mapper) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.modelMapper = modelMapper;
    this.mapper = mapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = userRepository.findByLogin(username);
    return user;
  }

  public UserDetails signUp(SignUpDto data) throws InvalidJwtException {

    if (userRepository.findByLogin(data.getLogin()) != null) {
      throw new InvalidJwtException("Username already exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    User newUser = new User(data.getLogin(), encryptedPassword, data.getRole());

    return userRepository.save(newUser);
  }

//  @Override
  public UserDto updateUser(Long userId, User user) {
    User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND));
    try {
      existingUser.setLogin(user.getLogin());
      existingUser.setPosts(user.getPosts());
      existingUser.setComments(user.getComments());
      User updatedUser = userRepository.save(existingUser);
      UserDto userDto = modelMapper.map(updatedUser, UserDto.class);

      return userDto;
    } catch (Exception e) {
      throw new RuntimeException(ErrorMessages.USER_NOT_UPDATED);
    }
  }

//  @Override
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessages.USER_NOT_FOUND));
    try {
      deletePostsForUser(user);
      userRepository.deleteById(userId);
    } catch (Exception e) {
      throw new RuntimeException(ErrorMessages.USER_NOT_DELETED);
    }
  }

  // this will delete comments for certain user automatically
  private void deletePostsForUser(User user) {
    List<Post> postList = user.getPosts();
    if (!Common.isNullOrEmpty(postList)) {
      for (Post post : postList) {
        postRepository.deleteById(post.getId());
      }
    }
  }
}