package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
}
