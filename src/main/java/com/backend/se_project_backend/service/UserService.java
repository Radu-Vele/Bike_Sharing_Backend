package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.utils.dto.UserDTO;
import com.backend.se_project_backend.utils.dto.UserEditDTO;

public interface UserService {
    User findUserByUsername(String username);

    boolean userByUsernameExists(String username);

    boolean userByEmailExists(String email);

    User register(UserDTO user);

    void delete(String username);

    User edit(UserEditDTO userEditDTO);

    User editStartRide(Ride ride, String username);

    User editEndRide(Ride ride, String username);
}