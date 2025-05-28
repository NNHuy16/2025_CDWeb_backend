package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String requirements;

    private String salary;

    private String location;

    private LocalDate postedDate;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status; // PENDING, APPROVED, CLOSED

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

}