package com.backend.se_project_backend.service;


import com.backend.se_project_backend.model.Bike;

import java.util.Optional;

public interface BikeService {

    Optional<Bike> bikeById(long bikeId);

    void create(Bike bike);

    void delete(long bikeId);

    boolean calculateRating(long bikeId, Double currentRating);
}
