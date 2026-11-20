package com.training.job_hunting.dtos;

import com.training.job_hunting.models.ContactTypeForReminder;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReminderRequest(
        @NotNull LocalDate reminderDate,
        String commentary,
        @NotNull ContactTypeForReminder contactTypeForReminder
) {
}
