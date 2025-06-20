package com.example.jobSeaching.dto.request;

import com.example.jobSeaching.dto.PasswordConfirmable;
import com.example.jobSeaching.service.validator.annotation.DifferentFromOldPassword;
import com.example.jobSeaching.service.validator.annotation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@DifferentFromOldPassword
@PasswordMatches(message = "Mật khẩu xác nhận không khớp") // Có thể thêm message tùy chỉnh
public class ChangePasswordRequest implements PasswordConfirmable {

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu mới không được để trống")
    private String confirmNewPassword;

    @Override
    public String getPassword() {
        return newPassword;
    }

    @Override
    public String getConfirmPassword() {
        return confirmNewPassword;
    }
}
