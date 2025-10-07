package com.api.sso.auth.entity;

import lombok.*;

import java.util.List;

@Builder
@Data
@Getter
@Setter
public class UserResponse {

    String message;
    UserEntity user;
    List<UserEntity> userList;
}
