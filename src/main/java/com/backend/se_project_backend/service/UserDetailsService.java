package com.backend.se_project_backend.service;

import com.backend.se_project_backend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.backend.se_project_backend.model.User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username " + username + " was not found."));

        return mapToUserDetails(user);
    }

    private UserDetails mapToUserDetails(com.backend.se_project_backend.model.User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.name())).collect(Collectors.toList());

        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}