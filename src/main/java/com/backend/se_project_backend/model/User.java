package com.backend.se_project_backend.model;

import javax.persistence.*;

import com.backend.se_project_backend.utils.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName; //unique username
    private String legalName;
    private String email;
    private String password;
    private boolean locked = false;
    private boolean enabled = true;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String userName, String legalName, String email, String password, UserRole role) {
        this.userName = userName;
        this.legalName = legalName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role.name());
        return Collections.singletonList(authority); //?
    }

    public String getPassword() {
        return password;
    }
}
