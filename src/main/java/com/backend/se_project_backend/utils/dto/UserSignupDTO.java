package com.backend.se_project_backend.utils.dto;

import lombok.Getter;
import lombok.Setter;

public class UserSignupDTO {
    private String username;
    private String email;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
