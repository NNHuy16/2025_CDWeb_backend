package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.VerificationToken;

public interface VerificationTokenService {
    void createToken(User user, String token);
    VerificationToken findByToken(String token);
    void deleteToken(VerificationToken token);
}
