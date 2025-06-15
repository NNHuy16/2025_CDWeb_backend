package com.example.jobSeaching.service;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String verificationLink);
    void sendEmail(String to, String subject, String content);
}
