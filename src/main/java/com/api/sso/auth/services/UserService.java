package com.api.sso.auth.services;

import com.api.sso.auth.models.UserModel;
import com.api.sso.auth.

import com.api.sso.auth.models.LoginResponse;
import com.api.sso.auth.services.UserServiceImpl;

public interface UserService {
    UserServiceImpl findAll();
    UserResponse findById(Long id);
    UserResponse save(UserModel userModel);
    String  delete(Long id);
    UserResponse update(Long id, UserModel updatedUserModel);
    UserResponse registerUser(UserModel userModel);
    LoginResponse loginUser(String username, String password);
}
