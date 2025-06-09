package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User existingUser);
    @Query("SELECT vt FROM VerificationToken vt WHERE vt.expiryDate < :now")
    List<VerificationToken> findAllExpired(@Param("now") LocalDateTime now);
}
