package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.JobStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobStatusHistoryRepository extends JpaRepository<JobStatusHistory, Long> {
}
