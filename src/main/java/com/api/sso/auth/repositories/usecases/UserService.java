package com.api.sso.auth.repositories.usecases;

import com.api.sso.auth.models.UserModel;

public interface UserService {
    UserModel register(String name, String email, String password);
    UserModel login(String email, String password);
}
