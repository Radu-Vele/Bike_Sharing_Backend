package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.service.UserRegistrationService;
import com.backend.se_project_backend.utils.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
@CrossOrigin
public class UserRegistrationController {

    private UserRegistrationService userRegistrationService;

    @PostMapping
    public String register(@RequestBody UserRegistrationRequest request) {
        return userRegistrationService.register(request);
    }
}
