package com.backend.se_project_backend.utils.dto;

import com.backend.se_project_backend.utils.UserRoleEnum;

public class UserDTO {
    private String username;
    private String email;
    private UserRoleEnum role;
    private String password;
    private String legalName;
    private String phoneNumber;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
