package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.utils.dto.AccountDTO;
import com.backend.se_project_backend.utils.dto.UserSignupDTO;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);

    boolean userExists(String username, String email);

    Account register(AccountDTO user);
}