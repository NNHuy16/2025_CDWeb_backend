package com.example.jobSeaching.controller;

import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UsersService userService;

    public UserController(UsersService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setRole(Role.USER);
        return ResponseEntity.ok(userService.createUsers(user));
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
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        // Bạn có thể kiểm tra quyền ở đây hoặc ở service
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Xóa user theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
