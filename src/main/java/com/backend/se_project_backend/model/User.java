package com.backend.se_project_backend.model;
import com.backend.se_project_backend.utils.UserRoleEnum;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
public class User implements UserDetails {
    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String username; //unique username

    @NonNull
    @Indexed(unique = true)
    private String email;

    @NonNull
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role; //embed roles for each user

    private boolean hasActiveRide = false;

    @DBRef
    private Ride currentRide;

    private String legalName;

    @DBRef
    private List<Ride> rideList; //TODO: Is this the right solution to avoid unbounded arrays in DB?

    private String phoneNumber;

    private boolean isLocked = false; //can lock the account

    public User(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    @NonNull
    public String getUsername() {
        return username;
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

    /**
     * TODO: Might help me with email validation
     */
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

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public UserRoleEnum getRole() {
        return role;
    }

    public boolean isHasActiveRide() {
        return hasActiveRide;
    }

    public Ride getCurrentRide() {
        return currentRide;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setRole(@NonNull UserRoleEnum role) {
        this.role = role;
    }

    public void setHasActiveRide(boolean hasActiveRide) {
        this.hasActiveRide = hasActiveRide;
    }

    public void setCurrentRide(Ride currentRide) {
        this.currentRide = currentRide;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public List<Ride> getRideList() {
        return rideList;
    }

    public void setRideList(List<Ride> rideList) {
        this.rideList = rideList;
    }

    public void addRide(Ride ride) {
        this.rideList.add(ride);
    }

    public void setId(String id) {
        this.id = id;
    }
}

