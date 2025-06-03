package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.ChangePasswordRequest;
import com.example.jobSeaching.dto.OTP.ConfirmChangeEmailDTO;
import com.example.jobSeaching.dto.OTP.RequestChangeEmailDTO;
import com.example.jobSeaching.entity.ActivationKey;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.service.EmployerService;
import com.example.jobSeaching.service.UsersService;
import com.example.jobSeaching.service.AdminService;
import com.example.jobSeaching.service.JobService;
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
    private final UsersService userService;


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
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request) {

        userService.changePassword(userDetails.getUsername(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/request-change-email")
    public ResponseEntity<String> requestChangeEmail(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody RequestChangeEmailDTO dto) {
        userService.requestChangeEmail(userDetails.getUsername(), dto.getNewEmail());
        return ResponseEntity.ok("Đã gửi OTP tới email mới.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm-change-email")
    public ResponseEntity<String> confirmChangeEmail(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody ConfirmChangeEmailDTO dto) {
        userService.confirmChangeEmail(userDetails.getUsername(), dto.getOtp());
        return ResponseEntity.ok("Đổi email thành công.");
    }


}




