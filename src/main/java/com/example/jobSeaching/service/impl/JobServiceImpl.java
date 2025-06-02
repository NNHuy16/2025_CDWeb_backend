package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.JobRequest;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.JobStatusHistory;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.JobStatus;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.repository.JobRepository;
import com.example.jobSeaching.repository.JobStatusHistoryRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobStatusHistoryRepository historyRepo;

    @Override
    public Job createJob(JobRequest jobRequest, Authentication authentication) {
        String email = SecurityUtil.getEmailFromAuth(authentication);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.EMPLOYER) {
            throw new AccessDeniedException("Only employers can post jobs.");
        }

        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setRequirements(jobRequest.getRequirements());
        job.setSalary(jobRequest.getSalary());
        job.setLocation(jobRequest.getLocation());
        job.setDeadline(jobRequest.getDeadline());
        job.setPostedDate(LocalDate.now());
        job.setStatus(JobStatus.PENDING);
        job.setEmployer(user);

        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public Job updateJobStatus(Long jobId, JobStatus newStatus, String adminEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobStatus currentStatus = job.getStatus();
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s", currentStatus, newStatus));
        }

        job.setStatus(newStatus);
        Job updatedJob = jobRepository.save(job);
        saveJobStatusHistory(job, currentStatus, newStatus, adminEmail);

        return updatedJob;
    }

    private boolean isValidTransition(JobStatus current, JobStatus target) {
        return switch (current) {
            case PENDING -> target == JobStatus.APPROVED || target == JobStatus.REJECTED;
            case APPROVED -> target == JobStatus.CLOSED;
            case REJECTED -> target == JobStatus.PENDING;
            default -> false;
        };
    }

    private void saveJobStatusHistory(Job job, JobStatus oldStatus, JobStatus newStatus, String userEmail) {
        JobStatusHistory history = new JobStatusHistory();
        history.setJob(job);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedByEmail(userEmail);
        history.setChangedAt(LocalDateTime.now());
        historyRepo.save(history);
    }
}
