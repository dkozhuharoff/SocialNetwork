package com.social.network.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.social.network.security.auth.SecurityFilter;

import static com.social.network.utils.constants.Paths.*;

@Configuration
@EnableWebSecurity
public class AuthConfig {
  private SecurityFilter securityFilter;

  public AuthConfig(SecurityFilter securityFilter) {
    this.securityFilter = securityFilter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, MAIN_AUTH_PATH + "/*").permitAll()
            .requestMatchers("/swagger-ui/*").permitAll()
            .requestMatchers("/v3/api-docs", "/v3/api-docs/*").permitAll()
            .requestMatchers(HttpMethod.GET, MAIN_CATEGORY_PATH).hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.GET, MAIN_CATEGORY_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.POST, MAIN_CATEGORY_PATH).hasAnyRole("ADMIN", "MODERATOR")
            .requestMatchers(HttpMethod.PUT, MAIN_CATEGORY_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR")
            .requestMatchers(HttpMethod.DELETE, MAIN_CATEGORY_PATH + "/*").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, MAIN_CATEGORYID_POST_PATH).hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.GET, MAIN_CATEGORYID_POST_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.POST, MAIN_CATEGORYID_POST_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.PUT, MAIN_POST_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.DELETE, MAIN_CATEGORYID_POST_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.GET, MAIN_POSTID_COMMENT_PATH).hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.GET, MAIN_POSTID_COMMENT_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.POST, MAIN_POSTID_COMMENT_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.PUT, MAIN_COMMENT_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .requestMatchers(HttpMethod.DELETE, MAIN_COMMENT_PATH + "/*").hasAnyRole("ADMIN", "MODERATOR", "USER")
            .anyRequest().authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}