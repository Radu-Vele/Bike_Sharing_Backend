package com.backend.se_project_backend.service;


import com.backend.se_project_backend.dto.BikeDTO;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;

import java.util.Optional;

public interface BikeService {

    Optional<Bike> bikeByExternalId(long externalId);

    /**
     * @return the external ID of the newly created bike
     */
    public long create(BikeDTO bikeDTO);

    public void delete(String bikeId);

    public void calculateRating(long externalId, Double currentRating, String username) throws Exception;
}
