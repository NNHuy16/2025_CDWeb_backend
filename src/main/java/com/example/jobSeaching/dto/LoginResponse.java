package com.example.jobSeaching.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

public class LoginResponse {
    private String token;
    private String roles;

    public LoginResponse(String token, String roles) {
        this.token = token;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getRoles() {
        return roles;
    }
}
