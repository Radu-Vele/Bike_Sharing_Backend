package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.config.jwt.JwtRequest;
import com.backend.se_project_backend.config.jwt.JwtResponse;
import com.backend.se_project_backend.config.jwt.JwtUtility;
import com.backend.se_project_backend.service.UserService;
import com.backend.se_project_backend.utils.UserRoleEnum;
import com.backend.se_project_backend.utils.dto.UserDTO;
import com.backend.se_project_backend.utils.dto.UserEditDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> logInUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        User userByUsername = this.userService.findUserByUsername(username);

        if (userByUsername.getRole().equals(UserRoleEnum.USER)) {
            return ResponseEntity.ok("USER");
        }
        else if(userByUsername.getRole().equals(UserRoleEnum.ADMIN)) {
            return ResponseEntity.ok("ADMIN");
        }
        //failed login
        return null;
    }

    @PostMapping("/authenticate")
    @CrossOrigin
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(userService.authenticate(jwtRequest));
    }

    //Sign up a new user & account
    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<?> signup(@RequestBody UserDTO user) {
        if (this.userService.userByUsernameExists(user.getUsername()) || this.userService.userByEmailExists(user.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        User registeredUser = this.userService.register(user);
        return new ResponseEntity<User>(registeredUser, HttpStatus.CREATED);
    }

    //return account details
    @GetMapping("/account-details")
    @CrossOrigin
    public User showAccountDetails(@RequestParam String username) {
        return this.userService.findUserByUsername(username);
    }

    @DeleteMapping("/delete-account")
    @CrossOrigin
    public ResponseEntity<?> deleteAccount(@RequestParam String username) {
        if (!this.userService.userByUsernameExists(username)) {
            return new ResponseEntity<>(username, HttpStatus.NOT_FOUND);
        }
        this.userService.delete(username);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PutMapping("/edit-account")
    @CrossOrigin
    public ResponseEntity<?> editAccount(@RequestBody UserEditDTO userEditDTO) {
        if(!this.userService.userByUsernameExists(userEditDTO.getUsername())) {
            return new ResponseEntity<>(userEditDTO.getUsername(), HttpStatus.NOT_FOUND);
        }
        User client = this.userService.edit(userEditDTO);
        return new ResponseEntity<User>(client, HttpStatus.CREATED);
    }
}
