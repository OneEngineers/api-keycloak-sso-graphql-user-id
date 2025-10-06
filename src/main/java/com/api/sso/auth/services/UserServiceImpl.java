package com.api.sso.auth.services;

import com.api.sso.auth.entity.UserEntity;
import com.api.sso.auth.models.LoginResponse;
import com.api.sso.auth.models.UserModel;
import com.api.sso.auth.models.UserResponse;
import com.api.sso.auth.repositories.UserRepositories;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserRepositories userRepository;

    @Autowired
    private KeycloakService keycloakService;

    @Value("${app.keycloak.user.client-id}")
    private String clientId;

    @Value("${app.keycloak.user.client-secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${app.keycloak.user.realm}")
    private String realm;

    private final RestTemplate restTemplate = new RestTemplate();

    // Find all users
    public UserServiceImpl findAll() {
        List<UserEntity> userEntity = userRepository.findAll();
        List<UserModel> userList = objectMapper.convertValue(userEntity, new TypeReference<List<UserModel>>() {});
        return UserResponse.builder()
                .userList(userList)
                .message("Successfully fetched the user list")
                .build();
    }

    public UserResponse findById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            UserModel user = objectMapper.convertValue(userEntity.get(), UserModel.class);
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

    // Save a new user
    public UserResponse save(UserModel user) {
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(user.getEmail());
            UserEntity savedUser = userRepository.save(userEntity);
            UserModel userModel = objectMapper.convertValue(savedUser, UserModel.class);
            return UserResponse.builder()
                    .user(userModel)
                    .message("User Saved Successfully")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user");
        }
    }

    // Delete a user by id
    public String delete(Long id) {
        try {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user");
        }
    }

    public UserResponse update(Long id, UserModel updatedUserModel) {
        try {
            // Check if an account exists
            Optional<UserEntity> existingUser = userRepository.findById(id);

            if (existingUser.isPresent()) {
                UserEntity userEntity = existingUser.get();

                // Update fields (set new values from the updatedUserEntity)
                if (updatedUserModel.getEmail() != null) {
                    userEntity.setEmail(updatedUserModel.getEmail());
                }
                userRepository.save(userEntity);
                UserModel updatedUser = objectMapper.convertValue(userEntity, UserModel.class);

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

    @Override
    public UserResponse registerUser(UserModel userModel) {
        keycloakService.createUser(userModel.getUsername(), userModel.getEmail(), userModel.getFirstName(), userModel.getLastName(), userModel.getPassword());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userModel.getUsername());
        userEntity.setEmail(userModel.getEmail());
        userEntity.setName(userModel.getFirstName() + " " + userModel.getLastName());
        userRepository.save(userEntity);
        return UserResponse.builder()
                .user(userModel)
                .message("User registered successfully")
                .build();
    }

    @Override
    public LoginResponse loginUser(String username, String password) {
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

        return restTemplate.postForObject(url, request, LoginResponse.class);
    }
}
