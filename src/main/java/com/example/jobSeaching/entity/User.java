package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.AuthProvider;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String keyId;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // nullable nếu đăng nhập qua OAuth

    @Column(unique = true)
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String logoUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false)
    private boolean enabled = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ActivationKey activationKey;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationToken verificationToken;

    @OneToMany(mappedBy = "user")
    private List<Application> applications;

    @PostPersist
    public void setKeyIdAfterInsert() {
        if (this.keyId == null) {
            this.keyId = "NHK" + this.id;
        }
    }
}


