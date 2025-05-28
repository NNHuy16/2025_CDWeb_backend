package com.example.jobSeaching.controller;

import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.service.EmployerService;
import com.example.jobSeaching.service.UsersService;
import com.example.jobSeaching.service.AdminService;
import com.example.jobSeaching.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UsersService userService;
    private final AdminService adminService;
    private final EmployerService employerService;
    private final JobService jobService;


    public UserController(UsersService userService, AdminService adminService, EmployerService employerService, JobService jobService) {
        this.userService = userService;
        this.adminService = adminService;
        this.employerService = employerService;
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(adminService.createUsers(user));
    }

// Lấy user theo id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lấy tất cả user, hoặc lọc theo role nếu truyền tham số role
    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) Role role) {
        List<User> users = userService.getAllUsers();
        if (role != null) {
            users = users.stream()
                    .filter(u -> u.getRole() == role)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(users);
    }

    // Cập nhật user (có thể cập nhật role khi admin thực hiện)
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, Authentication authentication) {
        String email = SecurityUtil.extractEmail(authentication);
        User updated = userService.updateUserProfile(email, updatedUser);
        return ResponseEntity.ok(updated);
    }


    // Xóa user theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
