package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Bike;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BikeRepository extends MongoRepository<Bike, String> {

}
