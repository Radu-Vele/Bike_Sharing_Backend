package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.model.UserRole;
import com.backend.se_project_backend.repository.UserRepository;
import com.backend.se_project_backend.repository.UserRoleRepository;
import com.backend.se_project_backend.utils.UserRoleEnum;
import com.backend.se_project_backend.utils.dto.UserSignupDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleService userRoleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            throw new IllegalStateException("Can not find user with this username");
        }
    }

    @Override
    public boolean userExists(String username, String email) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        return byUsername.isPresent() || byEmail.isPresent();
    }

    @Override
    public User register(UserSignupDTO user) {
        UserRole userRole = this.userRoleService.getUserRoleByEnumName(UserRoleEnum.USER);
        User newUser = this.modelMapper.map(user, User.class);
        newUser.setRoles(List.of(userRole));
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }
}
