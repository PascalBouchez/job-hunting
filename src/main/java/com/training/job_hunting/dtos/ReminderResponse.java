package com.training.job_hunting.dtos;

import com.training.job_hunting.models.ContactTypeForReminder;
import com.training.job_hunting.models.Reminder;

import java.time.LocalDate;

public record ReminderResponse(
        Long id,
        LocalDate reminderDate,
        String commentary,
        ContactTypeForReminder contactType,
        Long applicationId
) {
    public static ReminderResponse from(Reminder reminder) {
        return new ReminderResponse(
                reminder.getId(),
                reminder.getReminderDate(),
                reminder.getCommentary(),
                reminder.getContactType(),
                reminder.getApplication().getId()
        );
    }
}
