package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RidesRepository extends JpaRepository<Ride, Long> {

}

