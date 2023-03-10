package com.backend.se_project_backend.model;
import com.backend.se_project_backend.utils.UserRoleEnum;
import com.mongodb.lang.NonNull;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Document(collection = "users")
public class User {
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

    private List<UserRoleEnum> roles = new ArrayList<>(); //embed roles for each user

    private boolean activeRide = false; //TODO: to rename to hasActiveRide

    @DBRef
    private Ride currentRide; //TODO: Should it be here or in the Account?

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

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public List<UserRoleEnum> getRoles() {
        return roles;
    }

    public boolean isActiveRide() {
        return activeRide;
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

    public void setRoles(@NonNull List<UserRoleEnum> roles) {
        this.roles = roles;
    }

    public void setActiveRide(boolean activeRide) {
        this.activeRide = activeRide;
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

