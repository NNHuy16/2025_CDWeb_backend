package com.example.jobSeaching.service.validator;

import com.example.jobSeaching.dto.request.ChangePasswordRequest;
import com.example.jobSeaching.service.validator.annotation.DifferentFromOldPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentFromOldPasswordValidator implements ConstraintValidator<DifferentFromOldPassword, ChangePasswordRequest> {

    @Override
    public boolean isValid(ChangePasswordRequest dto, ConstraintValidatorContext context) {
        if (dto.getOldPassword() == null || dto.getNewPassword() == null) {
            return true; // Để NotBlank lo phần null
        }
        return !dto.getOldPassword().equals(dto.getNewPassword());
    }
}
