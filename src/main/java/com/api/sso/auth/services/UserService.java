package com.api.sso.auth.services;

import com.api.sso.auth.models.UserModel;
import com.api.sso.auth.models.UserResponse;

import com.api.sso.auth.models.LoginResponse;

public interface UserService {
    UserResponse findAll();
    UserResponse findById(Long id);
    UserResponse save(UserModel userModel);
    String  delete(Long id);
    UserResponse update(Long id, UserModel updatedUserModel);
    UserResponse registerUser(UserModel userModel);
    LoginResponse loginUser(String username, String password);
}
