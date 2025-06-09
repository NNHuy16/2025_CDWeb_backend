package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.ChangePasswordRequest;
import com.example.jobSeaching.dto.ChangeEmailRequest;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.service.BlacklistService;
import com.example.jobSeaching.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UsersService userService;

    @Autowired
    private BlacklistService blacklistService;

    public UserController(UsersService userService) {
        this.userService = userService;

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, Authentication authentication) {
        String email = SecurityUtil.extractEmail(authentication);
        User updated = userService.updateUserProfile(email, updatedUser);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // bỏ "Bearer "
        blacklistService.addToken(token);  // lưu token vào blacklist
        return ResponseEntity.ok("Logged out");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request) {

        userService.changePassword(userDetails.getUsername(), request.getOldPassword(), request.getNewPassword(), request.getConfirmNewPassword());
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PostMapping("/change-email")
    public ResponseEntity<String> requestEmailChange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangeEmailRequest request) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        userService.requestEmailChange(user, request);
        return ResponseEntity.ok("Đã gửi email xác nhận tới địa chỉ mới");
    }

    @GetMapping("/confirm-email-change")
    public ResponseEntity<String> confirmEmailChange(@RequestParam String token) {
        try {
            userService.confirmEmailChange(token);
            return ResponseEntity.ok("Email đã được thay đổi thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // trả 400 với message
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi trong quá trình xử lý.");
        }
    }



}




