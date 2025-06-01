package com.example.jobSeaching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationRequest {

    private String key;
    private boolean activated;
    // getters & setters
}
