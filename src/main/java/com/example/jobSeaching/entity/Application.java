package com.example.jobSeaching.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate applicationDate;

    private String status; // SENT, REVIEWED, REJECTED, INTERVIEW

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
