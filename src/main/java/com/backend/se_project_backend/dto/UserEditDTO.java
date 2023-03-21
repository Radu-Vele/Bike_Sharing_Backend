package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEditDTO { //TODO: Edit password

    @NotBlank
    private String legalName;

    @NotBlank
    private String phoneNumber;
}