package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.UserRepository;
import com.backend.se_project_backend.utils.dto.UserDTO;
import com.backend.se_project_backend.utils.dto.UserEditDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleService userRoleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public boolean userByUsernameExists(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        return byUsername.isPresent();
    }

    @Override
    public boolean userByEmailExists(String email) {
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    @Override
    public User register(UserDTO user) {
        User newUser = this.modelMapper.map(user, User.class);
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public void delete(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        user.ifPresent(value -> this.userRepository.deleteById(value.getId()));
    }

    @Override
    public User edit(UserEditDTO userEditDTO) {
        Optional<User> user = this.userRepository.findByUsername(userEditDTO.getUsername());
        if(user.isPresent()) {
            user.get().setLegalName(userEditDTO.getLegalName());
            user.get().setPhoneNumber(userEditDTO.getPhoneNumber());
            return userRepository.save(user.get());
        }
        return null;
    }

    @Override
    public User editStartRide(Ride ride, String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent()) {
            if (!user.get().isActiveRide()) {
                user.get().setCurrentRide(ride);
                user.get().setActiveRide(true);
                return userRepository.save(user.get());
            }
        }
        return null;
    }
    @Override
    public User editEndRide(Ride ride, String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent()) {
            if (user.get().isActiveRide()) {
                user.get().setActiveRide(false);
                return userRepository.save(user.get());
            }
        }
        return null;
    }
}
