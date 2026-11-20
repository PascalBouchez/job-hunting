package com.training.job_hunting.controllers;

import com.training.job_hunting.dtos.ReminderRequest;
import com.training.job_hunting.dtos.ReminderResponse;
import com.training.job_hunting.services.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public List<ReminderResponse> findByApplication(@PathVariable Long applicationId) {
        return reminderService.getRemindersForApplication(applicationId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@PathVariable Long applicationId, @RequestBody @Valid ReminderRequest reminder) {
        return reminderService.create(applicationId, reminder);
    }

    @PutMapping("/{reminderId}")
    public ReminderResponse update(@PathVariable Long applicationId, @PathVariable Long reminderId, @RequestBody @Valid ReminderRequest reminderRequest) {
        return reminderService.update(applicationId, reminderId, reminderRequest);
    }

    @DeleteMapping("/{reminderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long applicationId, @PathVariable Long reminderId) {
        reminderService.delete(applicationId, reminderId);
    }

}
