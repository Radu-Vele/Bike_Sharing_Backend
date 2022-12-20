package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.utils.EmailValidator;
import com.backend.se_project_backend.utils.UserRegistrationRequest;
import com.backend.se_project_backend.utils.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;

    public String register(UserRegistrationRequest request) {
        boolean validEmail = emailValidator.test(request.getEmail());
        if(!validEmail) {
            throw new IllegalStateException("The email address is not valid!");
        }
        return userService.signUpUser(
                new User (
                        request.getUserName(),
                        request.getLegalName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER
                )
        );
    }
}
