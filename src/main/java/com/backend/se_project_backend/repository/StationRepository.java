package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Station;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StationRepository extends MongoRepository<Station, String> {
    Optional<Station> findByName(String name);
}
