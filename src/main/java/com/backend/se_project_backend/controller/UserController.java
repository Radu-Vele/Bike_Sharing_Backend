package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.dto.UserCreatedDTO;
import com.backend.se_project_backend.dto.UserDetailsDTO;
import com.backend.se_project_backend.config.jwt.JwtRequest;
import com.backend.se_project_backend.config.jwt.JwtResponse;
import com.backend.se_project_backend.config.jwt.JwtUtility;
import com.backend.se_project_backend.service.UserService;
import com.backend.se_project_backend.dto.UserDTO;
import com.backend.se_project_backend.dto.UserEditDTO;
import com.backend.se_project_backend.utils.exceptions.UserAlreadyRegisteredException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtility jwtUtility;

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> logInUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) throws Exception{
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        return ResponseEntity.ok(this.userService.roleByUsername(username)); //TODO: validate the login mechanism
    }

    @PostMapping("/authenticate")
    @CrossOrigin
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody JwtRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(userService.authenticate(jwtRequest));
    }

    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO user) throws UserAlreadyRegisteredException {
        return new ResponseEntity<UserCreatedDTO>(this.userService.register(user), HttpStatus.CREATED);
    }

    @GetMapping("/account-details")
    @CrossOrigin
    public UserDetailsDTO showAccountDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) throws Exception{
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        return this.userService.getUserDetails(username);
    }

    @DeleteMapping("/delete-account")
    @CrossOrigin
    public ResponseEntity<?> deleteAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        this.userService.delete(username);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PutMapping("/edit-account")
    @CrossOrigin
    public ResponseEntity<?> editAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody UserEditDTO userEditDTO) throws Exception{
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        UserDetailsDTO editedDetails = this.userService.edit(username, userEditDTO);
        return new ResponseEntity<UserDetailsDTO>(editedDetails, HttpStatus.OK);
    }
}
