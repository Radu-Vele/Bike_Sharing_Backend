package com.backend.se_project_backend.repository;

import com.backend.se_project_backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
