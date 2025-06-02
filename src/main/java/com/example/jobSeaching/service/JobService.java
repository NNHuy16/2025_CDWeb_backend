package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.JobRequest;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.enums.JobStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JobService {
    Job createJob(JobRequest jobRequest, Authentication authentication);
    void deleteJob(Long id);
    List<Job> getAllJobs();
    Job updateJob(Job job);
    Job getJobById(Long id);
    Job updateJobStatus(Long jobId, JobStatus newStatus, String adminEmail);
}
