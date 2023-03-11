package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RideRepository extends MongoRepository<Ride, String> {

}

