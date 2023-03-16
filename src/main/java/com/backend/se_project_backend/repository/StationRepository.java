package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Station;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface StationRepository extends MongoRepository<Station, String> {
    Optional<Station> findByName(String name);

    @Query("{'latitude' : ?0, 'longitude' : ?1}")
    Optional<Object> findByCoordinates(Double latitude, Double longitude);
}
