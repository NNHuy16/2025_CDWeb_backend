package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.EmailChangeToken;
import com.example.jobSeaching.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailChangeTokenRepository extends JpaRepository<EmailChangeToken, Long> {
    Optional<EmailChangeToken> findByToken(String token);
    boolean existsByUserAndExpiryDateAfter(User user, LocalDateTime now);
    void deleteAllByUser(User user);
    boolean existsByUserAndCreatedDateAfter(User user, LocalDateTime dateTime);

}
