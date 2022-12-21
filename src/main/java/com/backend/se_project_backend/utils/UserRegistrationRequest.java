package com.backend.se_project_backend.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistrationRequest {
    private final String userName;
    private final String legalName;
    private final String email;
    private final String password;
}
