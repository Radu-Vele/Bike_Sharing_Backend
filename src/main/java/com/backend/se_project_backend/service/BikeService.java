package com.backend.se_project_backend.service;


import com.backend.se_project_backend.model.Bike;

import java.util.Optional;

public interface BikeService {

    Optional<Bike> bikeById(String bikeId);

    void create(Bike bike);

    void delete(String bikeId);

    boolean calculateRating(String bikeId, Double currentRating);
}
