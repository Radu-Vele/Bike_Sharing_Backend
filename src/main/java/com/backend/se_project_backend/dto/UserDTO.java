package com.backend.se_project_backend.dto;

import com.backend.se_project_backend.utils.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "The username must not be blank")
    private String username;

    @Email(message = "A valid email address is mandatory")
    private String email;

    @NotBlank(message = "A password is mandatory")
    private String password;

    @NotNull
    private String legalName;

    private String phoneNumber; //can be null (design decision)
}
