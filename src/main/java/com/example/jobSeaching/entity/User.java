package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.AuthProvider;
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

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Application> applications;

}


