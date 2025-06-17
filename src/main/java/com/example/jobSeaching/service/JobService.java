package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.request.JobRequest;
import com.example.jobSeaching.dto.response.JobDTO;
import com.example.jobSeaching.entity.enums.JobStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JobService {
    JobDTO createJob(JobRequest jobRequest, Authentication authentication);
    void deleteJob(Long id);
    List<JobDTO> getAllJobs();
    JobDTO updateJob(JobRequest jobRequest, Long jobId);
    JobDTO getJobById(Long id);
    JobDTO updateJobStatus(Long jobId, JobStatus newStatus, String adminEmail);
}
