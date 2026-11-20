package com.training.job_hunting.dtos;

import jakarta.validation.constraints.NotBlank;

public record EnterpriseRequest(
        @NotBlank String name,
        String headquarters
) {}