package com.api.sso.auth.models;

import lombok.*;

import java.util.List;

@Builder
@Data
@Getter
@Setter
public class UserResponse {

    String message;
    UserModel user;
    List<UserModel> userList;
}
