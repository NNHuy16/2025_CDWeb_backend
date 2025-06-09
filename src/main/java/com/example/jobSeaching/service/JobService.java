package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.JobRequest;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.enums.JobStatus;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job createJob(JobRequest jobRequest, Authentication authentication);
    void deleteJob(Long id);
    List<Job> getAllJobs();
    Job updateJob(Job job);
    Optional<Job> getJobById(Long id);
}
