package com.backend.se_project_backend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account extends User implements Serializable {
    private String legalName;
    private List<Ride> rideList;
    private String phoneNumber;
    private boolean isLocked = false; //can lock the account

    public Account() {}

    @Column(nullable = false, unique = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(nullable = false)
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

    @OneToMany(fetch = FetchType.LAZY)
    public List<Ride> getRideList() {
        return rideList;
    }

    public void setRideList(List<Ride> rideList) {
        this.rideList = rideList;
    }

    public void addRide(Ride ride) {
        this.rideList.add(ride);
    }
}
