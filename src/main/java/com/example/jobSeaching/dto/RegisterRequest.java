package com.example.jobSeaching.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    private String lastName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;


    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải không hợp lệ")
    private LocalDate dateOfBirth;

    //    @Pattern(regexp = "^(http|https)://.*$", message = "Avatar phải là URL hợp lệ", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String logoUrl; // tương ứng logoUrl

}

