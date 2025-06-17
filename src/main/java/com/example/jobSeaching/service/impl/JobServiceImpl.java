package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.request.JobRequest;
import com.example.jobSeaching.dto.response.JobDTO;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.JobStatusHistory;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.JobStatus;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.mapper.JobMapper;
import com.example.jobSeaching.repository.JobRepository;
import com.example.jobSeaching.repository.JobStatusHistoryRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.JobService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobStatusHistoryRepository historyRepo;
    private final JobMapper jobMapper;

    @Override
    @Transactional
    public JobDTO createJob(JobRequest jobRequest, Authentication authentication) {
        String email = SecurityUtil.getEmailFromAuth(authentication);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.EMPLOYER) {
            throw new AccessDeniedException("Only employers can post jobs.");
        }

        if (jobRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline must be in the future");
        }

        Job job = mapToJob(jobRequest, user);
        return jobMapper.toDTO(jobRepository.save(job));
    }

    private Job mapToJob(JobRequest req, User user) {
        Job job = new Job();
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setRequirements(req.getRequirements());
        job.setSalary(req.getSalary());
        job.setAge(req.getAge());
        job.setLocation(req.getLocation());
        job.setContact(req.getContact());
        job.setCompanyName(req.getCompanyName());
        job.setPosition(req.getPosition());
        job.setEducation(req.getEducation());
        job.setExperience(req.getExperience());
        job.setWorkTime(req.getWorkTime());
        job.setQuantity(req.getQuantity());
        job.setDeadline(req.getDeadline());
        job.setPostedDate(LocalDate.now());
        job.setStatus(JobStatus.PENDING);
        job.setEmployer(user);
        return job;
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }
        jobRepository.deleteById(id);
    }

    @Override
    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDTO updateJob(JobRequest request, Long jobId) {
        Job existing = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setRequirements(request.getRequirements());
        existing.setSalary(request.getSalary());
        existing.setAge(request.getAge());
        existing.setLocation(request.getLocation());
        existing.setContact(request.getContact());
        existing.setCompanyName(request.getCompanyName());
        existing.setPosition(request.getPosition());
        existing.setEducation(request.getEducation());
        existing.setExperience(request.getExperience());
        existing.setWorkTime(request.getWorkTime());
        existing.setQuantity(request.getQuantity());
        existing.setDeadline(request.getDeadline());

        return jobMapper.toDTO(jobRepository.save(existing));
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            log.warn("Job not found with id: {}", id);
            return null;
        }
        return jobMapper.toDTO(job);
    }

    @Override
    @Transactional
    public JobDTO updateJobStatus(Long jobId, JobStatus newStatus, String adminEmail) {
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

        log.info("Job status updated: jobId={}, oldStatus={}, newStatus={}, by={}",
                jobId, currentStatus, newStatus, adminEmail);

        return jobMapper.toDTO(updatedJob);
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
