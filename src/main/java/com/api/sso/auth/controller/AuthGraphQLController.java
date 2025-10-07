package com.api.sso.auth.controller;

import com.api.sso.auth.entity.UserEntity;
import com.api.sso.auth.repositories.UserRepository;
import com.api.sso.auth.services.JwtService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Controller
public class AuthGraphQLController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthGraphQLController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @MutationMapping
    public UserEntity register(@Argument Map<String, String> input) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(input.get("username"));
        newUser.setEmail(input.get("email"));
        newUser.setPassword(passwordEncoder.encode(input.get("password")));
        return userRepository.save(newUser);
    }

    @MutationMapping
    public Map<String, String> login(@Argument Map<String, String> input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.get("username"), input.get("password"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(input.get("username"));
        return Map.of("token", token);
    }

    @QueryMapping
    public UserEntity me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
