package com.backend.se_project_backend.service;

import com.backend.se_project_backend.config.jwt.JwtRequest;
import com.backend.se_project_backend.config.jwt.JwtResponse;
import com.backend.se_project_backend.dto.UserCreatedDTO;
import com.backend.se_project_backend.dto.UserDetailsDTO;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.dto.UserDTO;
import com.backend.se_project_backend.dto.UserEditDTO;
import com.backend.se_project_backend.utils.exceptions.UserAlreadyRegisteredException;

import javax.mail.MessagingException;

public interface UserService {
    User findUserByUsername(String username) throws Exception;

    boolean userByUsernameExists(String username);

    boolean userByEmailExists(String email);

    String roleByUsername(String username) throws Exception;

    UserCreatedDTO register(UserDTO user, boolean isAdmin) throws UserAlreadyRegisteredException, MessagingException;

    void delete(String username);

    UserDetailsDTO edit(String username, UserEditDTO userEditDTO) throws UserAlreadyRegisteredException;

    void editStartRide(Ride ride, String username) throws Exception;

    void editEndRide(Ride ride, String username) throws Exception;

    UserDetailsDTO getUserDetails(String username) throws Exception;

    JwtResponse authenticate(JwtRequest request) throws Exception;
}