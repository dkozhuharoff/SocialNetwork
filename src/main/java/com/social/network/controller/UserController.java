package com.social.network.controller;

import com.social.network.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.network.security.auth.TokenProvider;
import com.social.network.dto.SignInDto;
import com.social.network.dto.SignUpDto;
import com.social.network.dto.JwtDto;
import com.social.network.model.User;

import static com.social.network.utils.constants.Paths.MAIN_AUTH_PATH;

@RestController
@RequestMapping(MAIN_AUTH_PATH)
public class UserController {
  private AuthenticationManager authenticationManager;
  private UserServiceImpl userServiceImpl;
  private TokenProvider tokenService;

  public UserController(AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl, TokenProvider tokenService) {
    this.authenticationManager = authenticationManager;
    this.userServiceImpl = userServiceImpl;
    this.tokenService = tokenService;
  }

  @Operation(summary = "Sign up",
          description = "Sign up endpoint, create a new user.")
  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody SignUpDto data) {
    userServiceImpl.signUp(data);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Sign in",
          description = "Sign in endpoint, use your username and password, " +
                  "then the endpoint will provide you bearer-token for authorization. " +
                  "It will be needed for the other API requests.")
  @PostMapping("/signin")
  public ResponseEntity<JwtDto> signIn(@RequestBody SignInDto data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());

    var authUser = authenticationManager.authenticate(usernamePassword);

    var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());

    return ResponseEntity.ok(new JwtDto(accessToken));
  }
}