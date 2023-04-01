package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.DatesIntervalDTO;
import com.backend.se_project_backend.dto.RideGetDTO;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.dto.RideDTO;

import java.util.List;

public interface RideService {

    void create(Ride ride);

    void delete(String rideId);

    void endRide(String username) throws Exception;

    void startRide(String username, RideDTO ride) throws Exception;

    List<RideGetDTO> findRidesByUser(String username) throws Exception;

    Ride findRideById(String rideId);

    List<RideGetDTO> fetchAllRidesBetween(DatesIntervalDTO dateDTO);
}
