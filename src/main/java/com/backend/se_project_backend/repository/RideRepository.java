package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends MongoRepository<Ride, String> {
    @Query("{ 'endTime' : { $gte : ?0 , $lte : ?1  } }")
    Optional<List<Ride>> findRidesEndedSince(@Param("lowerBound") LocalDateTime lowerBound, @Param("upperBound") LocalDateTime upperBound);
}