package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityScan("com.example.jobSeaching.entity")
@Table(name = "activation_keys")
public class ActivationKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private boolean activated = false;

    @Column(name = "activation_key", unique = true, nullable = false)
    private String activationKey;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime activatedAt;

    private LocalDateTime expiredAt;

    @OneToOne
    private User user;

}
