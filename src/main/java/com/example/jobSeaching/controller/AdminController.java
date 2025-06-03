package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.ActivationRequest;
import com.example.jobSeaching.entity.ActivationKey;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.ActivationKeyRepository;
import com.example.jobSeaching.service.AdminService;
import com.example.jobSeaching.service.ActivationKeyService;
import com.example.jobSeaching.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private final UsersService userService;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final ActivationKeyService activationKeyService;

    public AdminController(UsersService userService, AdminService adminService, ActivationKeyService activationKeyService) {
        this.userService = userService;
        this.adminService = adminService;

        this.activationKeyService = activationKeyService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(adminService.createUsers(user));
    }

    // Lấy user theo id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lấy tất cả user, hoặc lọc theo role nếu truyền tham số role
    @PreAuthorize("hasRole('ADMIN')")
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

    // Xóa user theo id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/activation")
    public ResponseEntity<String> changeActivationStatus(@RequestBody ActivationRequest request) {
            System.out.println("Received: " + request);
            try {
                if (request.isActivated()) {
                    activationKeyService.activateKey(request);
                } else {
                    activationKeyService.deactivateKey(request.getKey());
                }
                return ResponseEntity.ok("Cập nhật trạng thái kích hoạt thành công");
            } catch (RuntimeException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        }

}
