package com.backend.se_project_backend.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserSignupDTO {

    private String username;

    private String email;

    private String password;
}
