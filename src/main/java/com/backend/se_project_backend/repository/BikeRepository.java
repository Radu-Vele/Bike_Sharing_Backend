package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<Bike, Long> {

}
