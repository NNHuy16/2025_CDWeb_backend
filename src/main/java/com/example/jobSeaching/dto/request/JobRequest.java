package com.example.jobSeaching.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobRequest {

    @NotBlank(message = "Tiêu đề công việc không được để trống")
    private String title;

    @NotBlank(message = "Mô tả công việc không được để trống")
    private String description;

    @NotBlank(message = "Yêu cầu công việc không được để trống")
    private String requirements;

    @NotBlank(message = "Lương làm việc không được để trống")
    private String salary;

    @NotBlank(message = "Địa điểm làm việc không được để trống")
    private String location;

    @NotBlank(message = "Liên hệ không được để trống")
    private String contact;

    private String companyName;

    @NotBlank(message = "Vị trí làm việc không được để trống")
    private String position;

    private String age;

    private String education;

    @Min(value = 1, message = "Số lượng nhân viên phải lớn hơn 0")
    private int quantity;

    private String workTime;

    private String recruitment;

    @NotBlank(message = "Kinh nghiệm không được để trống")
    private String experience;

    @NotNull(message = "Hạn nộp hồ sơ không được để trống")
    @Future(message = "Hạn nộp hồ sơ phải là một ngày trong tương lai")
    private LocalDate deadline;
}
