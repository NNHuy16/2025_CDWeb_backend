package com.example.jobSeaching.dto.OTP;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmChangeEmailDTO {

    @NotBlank(message = "OTP không được để trống")
    private String otp;
}