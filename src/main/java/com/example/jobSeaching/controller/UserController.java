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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UsersService userService;
    private final AdminService adminService;
    private final JobService jobService;



    public UserController(UsersService userService, AdminService adminService, EmployerService employerService, JobService jobService) {
        this.userService = userService;
        this.adminService = adminService;
        this.jobService = jobService;
    }

    // Cập nhật user (có thể cập nhật role khi admin thực hiện)
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, Authentication authentication) {
        String email = SecurityUtil.extractEmail(authentication);
        User updated = userService.updateUserProfile(email, updatedUser);
        return ResponseEntity.ok(updated);
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request) {

        userService.changePassword(userDetails.getUsername(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PostMapping("/request-change-email")
    public ResponseEntity<String> requestChangeEmail(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody RequestChangeEmailDTO dto) {
        userService.requestChangeEmail(userDetails.getUsername(), dto.getNewEmail());
        return ResponseEntity.ok("Đã gửi OTP tới email mới.");
    }

    @PostMapping("/confirm-change-email")
    public ResponseEntity<String> confirmChangeEmail(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody ConfirmChangeEmailDTO dto) {
        userService.confirmChangeEmail(userDetails.getUsername(), dto.getOtp());
        return ResponseEntity.ok("Đổi email thành công.");
    }


}




