package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.LoginRequest;
import com.example.jobSeaching.dto.LoginResponse;
import com.example.jobSeaching.dto.RegisterRequest;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.security.JwtTokenProvider;
import com.example.jobSeaching.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsersService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không đúng");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            User savedUser = userService.registerUserLocal(registerRequest);
            savedUser.setPassword(null);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/login/google")
    public void loginWithGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

}
