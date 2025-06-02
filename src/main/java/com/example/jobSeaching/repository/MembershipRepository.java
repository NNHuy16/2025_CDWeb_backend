package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.Membership;
import com.example.jobSeaching.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUser(User user);

}
