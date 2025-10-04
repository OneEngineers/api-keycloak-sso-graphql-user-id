package com.api.sso.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {
    private long id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
