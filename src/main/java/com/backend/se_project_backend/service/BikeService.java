package com.backend.se_project_backend.service;


import com.backend.se_project_backend.model.Bike;

public interface BikeService {

    void create(Bike bike);

    void delete(long bikeId);
}
