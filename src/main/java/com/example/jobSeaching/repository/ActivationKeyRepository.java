package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.ActivationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationKeyRepository extends JpaRepository<ActivationKey, Long> {
    Optional<ActivationKey> findByActivationKeyIgnoreCase(String activationKey);
}
