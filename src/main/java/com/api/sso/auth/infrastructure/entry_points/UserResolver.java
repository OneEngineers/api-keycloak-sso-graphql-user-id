package com.api.sso.auth.infrastructure.entry_points;

import com.api.sso.auth.domain.models.User;
import com.api.sso.auth.domain.usecases.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserResolver {

    private final UserService userService;

    public UserResolver(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public List<User> users() {
        return null;
    }

    @MutationMapping
    public User register(@Argument String name, @Argument String email, @Argument String password) {
        return userService.register(name, email, password);
    }

    @MutationMapping
    public User login(@Argument String email, @Argument String password) {
        return userService.login(email, password);
    }
}
