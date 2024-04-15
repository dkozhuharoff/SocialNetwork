package com.social.network.service;

import com.social.network.dto.SignUpDto;
import com.social.network.dto.UserDto;
import com.social.network.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
     UserDetails loadUserByUsername(String username);
     UserDetails signUp(SignUpDto data);
     UserDto updateUser(Long userId, User user);
     void deleteUser(Long userId);
}
