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
import com.backend.se_project_backend.utils.enums.UserRoleEnum;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import com.backend.se_project_backend.utils.exceptions.UserAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Optional;

import static com.backend.se_project_backend.utils.StringConstants.EMAIL_CONFIRM_BODY;
import static com.backend.se_project_backend.utils.StringConstants.EMAIL_CONFIRM_SUBJECT;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

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
    public UserCreatedDTO register(UserDTO user, boolean isAdmin) throws UserAlreadyRegisteredException, MessagingException {
        if (this.userByUsernameExists(user.getUsername()) || this.userByEmailExists(user.getEmail())) {
            throw new UserAlreadyRegisteredException("Username or email address already in use.");
        }

        User newUser = this.modelMapper.map(user, User.class);
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        if (isAdmin) {
            newUser.setRole(UserRoleEnum.ADMIN);
        }
        else {
            newUser.setRole(UserRoleEnum.USER);
        }

        this.emailService.send(newUser.getEmail(), EMAIL_CONFIRM_SUBJECT, EMAIL_CONFIRM_BODY);

        return this.modelMapper.map(this.userRepository.save(newUser), UserCreatedDTO.class);
    }

    @Override
    public JwtResponse authenticate(JwtRequest request) throws Exception { //TODO: Make sure to catch all possible exceptions
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

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
    public void editStartRide(Ride ride, String username) throws Exception {
        Optional<User> user = this.userRepository.findByUsername(username);

        if(user.isEmpty()) {
            throw new DocumentNotFoundException("There is no user in the database with the given username.");
        }
        if (user.get().isInActiveRide()) {
            throw new IllegalOperationException("The user already has an active ride. Can not start a new one.");
        }
        user.get().setCurrentRide(ride);
        user.get().setInActiveRide(true);
        userRepository.save(user.get());
    }

    @Override
    public void editEndRide(Ride ride, String username) throws Exception {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new DocumentNotFoundException("There is no user in the database with the given username.");
        }
        if (!user.get().isInActiveRide()) {
            throw new IllegalOperationException("The user has no active ride. Can not end one.");
        }

        user.get().setInActiveRide(false);
        user.get().setCurrentRide(null);

        if(user.get().getRideList() == null) {
            ArrayList<Ride> newRidesList = new ArrayList<>();
            newRidesList.add(ride);
            user.get().setRideList(newRidesList);
        }
        else {
            user.get().getRideList().add(ride);
        }

        userRepository.save(user.get());
    }

    @Override
    public UserDetailsDTO getUserDetails(String username) throws Exception{
        User user = this.findUserByUsername(username);
        return this.modelMapper.map(user, UserDetailsDTO.class);
    }
}
