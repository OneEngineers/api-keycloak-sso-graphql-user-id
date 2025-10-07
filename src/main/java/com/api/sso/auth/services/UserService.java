package com.api.sso.auth.services;

import com.api.sso.auth.entity.UserEntity;

import com.api.sso.auth.entity.UserResponse;
import com.api.sso.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserEntity user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public UserResponse save(UserEntity user) {
        UserEntity savedUser = registerUser(user);
        return UserResponse.builder()
                .user(savedUser)
                .message("User saved successfully")
                .build();
    }
    public UserResponse findById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            UserEntity user = objectMapper.convertValue(userEntity.get(), UserEntity.class);
            return UserResponse.builder()
                    .user(user)
                    .message("Fetched user successfully for id - " + id)
                    .build();
        } else {
            return UserResponse.builder()
                    .message("User not found for id - " + id)
                    .build();
        }
    }
}
