package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.EmailChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequest, Long> {
    Optional<EmailChangeRequest> findByCurrentEmail(String currentEmail);
    void deleteByCurrentEmail(String currentEmail);
}
