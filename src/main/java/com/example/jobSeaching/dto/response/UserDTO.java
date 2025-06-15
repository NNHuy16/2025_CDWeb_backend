package com.example.jobSeaching.dto.response;

import com.example.jobSeaching.entity.enums.AuthProvider;
import com.example.jobSeaching.entity.enums.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String logoUrl;
    private Role role;
    private boolean enabled;
}
