package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final String USER_NOT_FOUND_MSG = "User with username %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, userName)));
    }

    public String signUpUser(User user) {
        boolean anotherUserFound = userRepository
                .findByUserName(user.getUsername())
                .isPresent();
        if(anotherUserFound) {
            throw new IllegalStateException("Username already taken.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //TODO: Send confirmation as a web page link
        userRepository.save(user);
        return "Works well";
    }
}
