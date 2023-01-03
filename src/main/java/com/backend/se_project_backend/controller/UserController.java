package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.service.UserDetailsService;
import com.backend.se_project_backend.security.jwt.JwtRequest;
import com.backend.se_project_backend.security.jwt.JwtResponse;
import com.backend.se_project_backend.security.jwt.JwtUtility;
import com.backend.se_project_backend.service.UserService;
import com.backend.se_project_backend.utils.UserRoleEnum;
import com.backend.se_project_backend.utils.dto.AccountDTO;
import com.backend.se_project_backend.utils.dto.UserSignupDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    @CrossOrigin
    public String logInUser(@RequestParam String username) {
        User userByUsername = this.userService.findUserByUsername(username);
        if (userByUsername.getRoles().stream()
                .anyMatch(u -> u.getRole().equals(UserRoleEnum.USER))) {
            return "USER";
        }
        return null;
    }

    @PostMapping("/authenticate")
    @CrossOrigin
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails
                = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        return new JwtResponse(token);
    }

    //Sign up a new user & account
    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<?> signup(@RequestBody AccountDTO user) {
        if (this.userService.userExists(user.getUsername(), user.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        Account client = this.userService.register(user);
        return new ResponseEntity<Account>(client, HttpStatus.CREATED);
    }

    //return account details
    @GetMapping("/account-details")
    public Account showAccountDetails(@RequestParam String username) {
        return this.userService.findAccountByUsername(username);
    }
}
