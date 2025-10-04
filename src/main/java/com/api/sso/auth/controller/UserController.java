package com.api.sso.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @QueryMapping
    public UserResponse users() {
        return userService.findAll();
    }

    @QueryMapping
    public UserResponse user(@Argument Long id) {
        return userService.findById(id);
    }

    @MutationMapping
    public UserResponse createUser(@Argument String name, @Argument String email) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        return userService.save(user);
    }

    @MutationMapping
    public UserResponse updateUser(@Argument Long id, @Argument String name, @Argument String email) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        return userService.update(id, user);
    }

    @MutationMapping
    public String deleteUser(@Argument Long id) {
        return userService.delete(id);
    }

    @MutationMapping
    public UserResponse registerUser(@Argument String username, @Argument String email, @Argument String firstName, @Argument String lastName, @Argument String password) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        return userService.registerUser(user);
    }

    @MutationMapping
    public LoginResponse loginUser(@Argument String username, @Argument String password) {
        return userService.loginUser(username, password);
    }
}
