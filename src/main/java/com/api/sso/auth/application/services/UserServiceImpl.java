package com.api.sso.auth.application.services;

import com.api.sso.auth.domain.models.User;
import com.api.sso.auth.domain.repositories.UserRepository;
import com.api.sso.auth.domain.usecases.UserService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String name, String email, String password) {
        String hashedPassword = new String(Base64.getEncoder().encode(password.getBytes()));
        User user = new User(null, name, email, hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String hashedPassword = new String(Base64.getEncoder().encode(password.getBytes()));
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }
        return null;
    }
}
