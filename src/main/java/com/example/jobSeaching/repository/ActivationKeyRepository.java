package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.ActivationKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationKeyRepository extends JpaRepository<ActivationKey, Long> {
    Optional<ActivationKey> findByActivationKey(String activationKey);
}
