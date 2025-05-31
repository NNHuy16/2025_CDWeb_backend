package com.example.jobSeaching.controller;

import com.example.jobSeaching.entity.ActivationKey;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.ActivationKeyRepository;
import com.example.jobSeaching.service.AdminService;
import com.example.jobSeaching.service.ActivationKeyService;
import com.example.jobSeaching.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UsersService userService;
    private final AdminService adminService;
    private final ActivationKeyService activationKeyService;
    private final ActivationKeyRepository activationKeyRepository;

    public AdminController(UsersService userService, AdminService adminService, ActivationKeyService activationKeyService, ActivationKeyRepository activationKeyRepository) {
        this.userService = userService;
        this.adminService = adminService;
        this.activationKeyService = activationKeyService;
        this.activationKeyRepository = activationKeyRepository;
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

    // Xóa user theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activation-keys")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ActivationKey> getAllKeys() {
        return activationKeyRepository.findAll();
    }

    @DeleteMapping("/activation-keys/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteKey(@PathVariable Long id) {
        if (!activationKeyRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key không tồn tại");
        }
        activationKeyRepository.deleteById(id);
        return ResponseEntity.ok("Đã xóa key");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/activation-keys/activate/{key}")
    public ResponseEntity<?> activateKey(@PathVariable String key) {
        activationKeyService.activateKey(key);
        return ResponseEntity.ok("Key đã được kích hoạt");
    }

}
