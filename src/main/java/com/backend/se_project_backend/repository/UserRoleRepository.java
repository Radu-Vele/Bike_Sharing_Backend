package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.model.UserRole;
import com.backend.se_project_backend.utils.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(UserRoleEnum userRoleEnum);
}
