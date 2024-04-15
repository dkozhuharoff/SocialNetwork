package com.social.network.configuration;

import com.social.network.repository.CategoryRepository;
import com.social.network.repository.PostRepository;
import com.social.network.repository.UserRepository;
import com.social.network.security.auth.TokenProvider;
import com.social.network.service.*;
import com.social.network.utils.Mapper;
import com.social.network.repository.CommentRepository;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {
  private UserRepository userRepository;

  private final CategoryRepository categoryRepository;
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  public AppConfig(UserRepository userRepository, CategoryRepository categoryRepository, CommentRepository commentRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
  }

  @Bean
  public OpenAPI defineOpenApi() {
    Server server = new Server();
    server.setUrl("http://localhost:8080");
    server.setDescription("Development");

    Contact myContact = new Contact();
    myContact.setName("Jane Doe");
    myContact.setEmail("your.email@gmail.com");

    Info information = new Info()
            .title("Employee Management System API")
            .version("1.0")
            .description("This API exposes endpoints to manage employees.")
            .contact(myContact);
    List<Server> servers = new ArrayList<>();
    servers.add(server);
    return new OpenAPI().servers(servers);
  }

  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider();
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public Mapper mapper() {
    return new Mapper(modelMapper());
  }

  @Bean
  public UserServiceImpl userServiceImpl() {
    return new UserServiceImpl(userRepository, postRepository, commentRepository, modelMapper(), mapper());
  }

  @Bean
  public CategoryService categoryService() {
    return new CategoryServiceImpl(categoryRepository, postRepository, modelMapper(), mapper());
  }

  @Bean
  public PostService postService() {
    return new PostServiceImpl(userServiceImpl(), postRepository, categoryService(), modelMapper(), mapper());
  }

  @Bean
  public CommentService commentService() {
    return new CommentServiceImpl(postService(), userServiceImpl(), commentRepository, modelMapper(), mapper());
  }
}