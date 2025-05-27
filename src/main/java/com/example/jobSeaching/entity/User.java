package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.AuthProvider;
import com.example.jobSeaching.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // nullable nếu đăng nhập qua OAuth

    @Column(unique = true)
    private String phoneNumber;

    private String logoUrl;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Application> applications;

}


