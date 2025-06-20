package com.example.jobSeaching.service.validator.annotation;

import com.example.jobSeaching.service.validator.DifferentFromOldPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DifferentFromOldPasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentFromOldPassword {
    String message() default "Mật khẩu mới phải khác mật khẩu cũ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
