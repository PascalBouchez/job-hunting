package com.training.job_hunting.exceptions;

public class ReminderNotFoundException extends ResourceNotFoundException {
    public ReminderNotFoundException(Long reminderId) {
        super("Reminder", reminderId);
    }
}
