package com.example.jobSeaching.dto;

import com.example.jobSeaching.service.validator.annotation.DifferentFromOldPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@DifferentFromOldPassword
@Data
public class ChangeEmailRequest {
    @NotBlank(message = "Email mới không được để trống")
    @Email(message = "Email mới không hợp lệ")
    private String newEmail;

    @NotBlank(message = "Mật khẩu không được để trống") // nếu bạn yêu cầu xác minh
    private String password;
}
