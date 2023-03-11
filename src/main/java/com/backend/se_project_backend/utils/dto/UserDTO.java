package com.backend.se_project_backend.utils.dto;

import com.backend.se_project_backend.utils.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String username;

    private String email;

    private UserRoleEnum role;

    private String password;

    private String legalName;

    private String phoneNumber;
}
