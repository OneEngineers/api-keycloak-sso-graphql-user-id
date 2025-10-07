package com.api.sso.auth.services;

import com.api.sso.auth.entity.UserEntity;

import com.api.sso.auth.entity.UserResponse;
import com.api.sso.auth.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Value("${app.keycloak.user.client-id}")
    private String clientId;

    @Value("${app.keycloak.user.client-secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${app.keycloak.user.realm}")
    private String realm;

    private final RestTemplate restTemplate = new RestTemplate();
    // @Override
    public UserResponse registerUser(UserEntity userModel) {
        keycloakService.createUser(userModel.getUsername(), userModel.getEmail(), userModel.getFirstName(), userModel.getLastName(), userModel.getPassword());
        com.api.sso.auth.entity.UserEntity userEntity = new com.api.sso.auth.entity.UserEntity();
        userEntity.setUsername(userModel.getUsername());
        userEntity.setEmail(userModel.getEmail());
        userRepository.save(userEntity);
        return UserResponse.builder()
                .user(userModel)
                .message("User registered successfully")
                .build();
    }

    // @Override
    public UserResponse loginUser(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(url, request, UserResponse.class);
    }

    public UserEntity login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String hashedPassword = passwordEncoder.encode(password);
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }
        return null;
    }
    public UserResponse save(UserEntity user) {
        try {
           UserEntity userEntity = new UserEntity();
            userEntity.setEmail(user.getEmail());
            UserEntity savedUser = userRepository.save(userEntity);
            UserEntity userModel = objectMapper.convertValue(savedUser, UserEntity.class);
            return UserResponse.builder()
                    .user(userModel)
                    .message("User Saved Successfully")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user");
        }
    }

//    public UserResponse save(UserEntity user) {
//        UserEntity savedUser = registerUser(user);
//        return UserResponse.builder()
//                .user(savedUser)
//                .message("User saved successfully")
//                .build();
//    }
    public UserEntity findAll() {
        List<UserEntity> userEntity = userRepository.findAll();
        List<UserEntity> userList = objectMapper.convertValue(userEntity, new TypeReference<List<UserEntity>>() {});
        return UserResponse.builder()
                .userList(userList)
                .message("Successfully fetched the user list")
                .build().getUser();
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
    // Delete a user by id
    public String delete(Long id) {
        try {
            userRepository.delete(id);
            return "User deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user");
        }
    }
    public UserResponse update(Long id, UserEntity updatedUserEntity) {
        try {
            // Check if an account exists
            Optional<com.api.sso.auth.entity.UserEntity> existingUser = userRepository.findById(id);

            if (existingUser.isPresent()) {
                com.api.sso.auth.entity.UserEntity userEntity = existingUser.get();

                // Update fields (set new values from the updatedUserEntity)
                if (updatedUserEntity.getEmail() != null) {
                    userEntity.setEmail(updatedUserEntity.getEmail());
                }
                userRepository.save(userEntity);
                UserEntity updatedUser = objectMapper.convertValue(userEntity, UserEntity.class);

                return UserResponse.builder()
                        .user(updatedUser)
                        .message("User updated successfully for id - " + id)
                        .build();
            } else {
                return UserResponse.builder()
                        .message("User not found for id - " + id)
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

}
