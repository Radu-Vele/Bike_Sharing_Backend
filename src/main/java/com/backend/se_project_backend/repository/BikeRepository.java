package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Bike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BikeRepository extends MongoRepository<Bike, String> {
    @Query("{'externalId' : ?0}")
    Optional<Bike> findByExternalId(long externalId);

    @Query("{'usable' : false}")
    List<Bike> findAllNonUsable();

    @Query("{'usable' : true}")
    List<Bike> findAllUsable();
}

