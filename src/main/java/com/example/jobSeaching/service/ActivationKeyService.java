package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.ActivationRequest;

public interface ActivationKeyService {
    void activateKey(ActivationRequest request);
    void deactivateKey(String key);
}
