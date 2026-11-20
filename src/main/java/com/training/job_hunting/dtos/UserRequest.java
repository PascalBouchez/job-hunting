package com.training.job_hunting.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank @Size(min=8) String password
) {}
