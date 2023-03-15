package com.backend.se_project_backend.service;


import com.backend.se_project_backend.dto.BikeDTO;
import com.backend.se_project_backend.model.Bike;

import java.util.Optional;

public interface BikeService {

    Optional<Bike> bikeByExternalId(long externalId);

    /**
     * @return the external ID of the newly created bike
     */
    public long create(BikeDTO bikeDTO);

    void delete(String bikeId);

    boolean calculateRating(long externalId, Double currentRating);
}
