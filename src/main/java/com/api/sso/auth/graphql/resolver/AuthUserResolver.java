package com.api.sso.auth.graphql.resolver;

import com.api.sso.auth.entity.UserEntity;
import com.api.sso.auth.repositories.UserRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthUserResolver {
    private final UserRepository userRepository;
    public AuthUserResolver(UserRepository userRepositories){
        this.userRepository = userRepositories;
    }

    @QueryMapping
    public List<UserEntity> findAll(){
        userRepository.findAll();
        return null;
    }

}
