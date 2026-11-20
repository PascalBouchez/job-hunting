package com.training.job_hunting.dtos;

import com.training.job_hunting.models.ApplicationStatus;
import com.training.job_hunting.validation.ApplicationDateRequiredWhenSent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ApplicationDateRequiredWhenSent
public record ApplicationRequest(
        @NotBlank(message = "Job title is required") String jobTitle,
        String jobDescription,
        @NotBlank String offerLink,
        Long monthlySalary,
        String location,
        LocalDate applicationDate,
        String notes,
        ApplicationStatus status,
        @NotNull Long enterpriseId
) {}
