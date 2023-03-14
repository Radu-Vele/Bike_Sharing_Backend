package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.dto.RideDTO;

import java.util.List;

public interface RideService {

    void create(Ride ride);
    void delete(String rideId);
    boolean endRide(String rideId);
    boolean startRide(RideDTO ride);
    List<Ride> findRidesByUser(String username);
    Ride findRideById(String rideId);


}
