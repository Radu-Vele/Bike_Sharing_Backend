package com.backend.se_project_backend.model;
import com.backend.se_project_backend.utils.enums.UserRoleEnum;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role; //embed roles for each user

    private boolean inActiveRide = false;

    @DBRef
    private Ride currentRide;

    private String legalName;

    @DBRef
    private List<Ride> rideList; //TODO: Is this the right solution to avoid unbounded arrays in DB?

    private String phoneNumber;

    private boolean isLocked = false; //can lock the account

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    /**
     * Return the list of authorities associated to the user's role
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public void addRide(Ride ride) {
        this.rideList.add(ride);
    }
}