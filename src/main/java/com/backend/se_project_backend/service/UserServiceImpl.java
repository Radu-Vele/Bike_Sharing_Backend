package com.backend.se_project_backend.service;

import com.backend.se_project_backend.config.jwt.JwtRequest;
import com.backend.se_project_backend.config.jwt.JwtResponse;
import com.backend.se_project_backend.config.jwt.JwtUtility;
import com.backend.se_project_backend.dto.UserCreatedDTO;
import com.backend.se_project_backend.dto.UserDetailsDTO;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.UserRepository;
import com.backend.se_project_backend.dto.UserDTO;
import com.backend.se_project_backend.dto.UserEditDTO;
import com.backend.se_project_backend.utils.exceptions.UserAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtility jwtUtility;

    @Override
    public User findUserByUsername(String username) throws Exception{
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            throw new UsernameNotFoundException("Can not find user with this username");
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
    public String roleByUsername(String username) throws Exception{
        User userByUsername = this.findUserByUsername(username);
        return userByUsername.getRole().toString();
    }

    @Override
    public UserCreatedDTO register(UserDTO user) throws UserAlreadyRegisteredException {
        if (this.userByUsernameExists(user.getUsername()) || this.userByEmailExists(user.getEmail())) {
            throw new UserAlreadyRegisteredException("Username or email address already in use.");
        }

        User newUser = this.modelMapper.map(user, User.class);
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.modelMapper.map(this.userRepository.save(newUser), UserCreatedDTO.class);
    }

    @Override
    public JwtResponse authenticate(JwtRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        final String token = jwtUtility.generateToken(user);
        return new JwtResponse(token);
    }

    @Override
    public void delete(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
         this.userRepository.deleteById(user.get().getId());
    }

    @Override
    public UserDetailsDTO edit(String username, UserEditDTO userEditDTO) throws UserAlreadyRegisteredException {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent()) {
            if(this.userByUsernameExists(user.get().getUsername())) {
                throw new UserAlreadyRegisteredException();
            }
            if(this.userByEmailExists(user.get().getEmail())) {
                throw new UserAlreadyRegisteredException();
            }

            user.get().setUsername(userEditDTO.getUsername());
            user.get().setLegalName(userEditDTO.getLegalName());
            user.get().setPhoneNumber(userEditDTO.getPhoneNumber());
            return this.modelMapper.map(userRepository.save(user.get()), UserDetailsDTO.class);
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public User editStartRide(Ride ride, String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent()) {
            if (!user.get().isHasActiveRide()) {
                user.get().setCurrentRide(ride);
                user.get().setHasActiveRide(true);
                return userRepository.save(user.get());
            }
        }
        return null;
    }

    @Override
    public User editEndRide(Ride ride, String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent()) {
            if (user.get().isHasActiveRide()) {
                user.get().setHasActiveRide(false);
                return userRepository.save(user.get());
            }
        }
        return null;
    }

    @Override
    public UserDetailsDTO getUserDetails(String username) throws Exception{
        User user = this.findUserByUsername(username);
        return this.modelMapper.map(user, UserDetailsDTO.class);
    }
}
