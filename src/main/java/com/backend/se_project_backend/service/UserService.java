package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.utils.dto.AccountDTO;
import com.backend.se_project_backend.utils.dto.UserEditDTO;
import com.backend.se_project_backend.utils.dto.UserSignupDTO;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);

    boolean userByUsernameExists(String username);

    boolean userByEmailExists(String email);

    Account register(AccountDTO user);

    Account findAccountByUsername(String username);

    void delete(String username);

    Account edit(UserEditDTO userEditDTO);
}