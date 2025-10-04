package com.api.sso.auth.domain.usecases;

import com.api.sso.auth.domain.models.User;

public interface UserService {
    User register(String name, String email, String password);
    User login(String email, String password);
}
