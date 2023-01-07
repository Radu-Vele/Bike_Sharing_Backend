package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.utils.dto.RideDTO;

public interface RideService {

    void create(Ride ride);
    void delete(long rideId);
    Ride endRide(Ride ride);

    Ride startRide(RideDTO ride);
}
