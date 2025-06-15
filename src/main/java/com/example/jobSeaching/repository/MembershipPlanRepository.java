package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.MembershipPlan;
import com.example.jobSeaching.entity.enums.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, MembershipType> {
}