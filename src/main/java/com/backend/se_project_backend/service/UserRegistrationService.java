package com.backend.se_project_backend.service;

import com.backend.se_project_backend.utils.UserRegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    public String register(UserRegistrationRequest request) {
        return "SuccessMessage :)";
    }
}
