package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Station;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StationRepository extends MongoRepository<Station, String> {

}
