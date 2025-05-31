package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class JobStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Job job;

    @Enumerated(EnumType.STRING)
    private JobStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private JobStatus newStatus;

    private String changedByEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changedAt;


}
