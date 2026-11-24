package com.training.job_hunting.services;

import com.training.job_hunting.dtos.ReminderRequest;
import com.training.job_hunting.dtos.ReminderResponse;
import com.training.job_hunting.exceptions.ReminderNotFoundException;
import com.training.job_hunting.models.Application;
import com.training.job_hunting.models.Reminder;
import com.training.job_hunting.repositories.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ApplicationService applicationService;

    public ReminderService(ReminderRepository reminderRepository, ApplicationService applicationService) {
        this.reminderRepository = reminderRepository;
        this.applicationService = applicationService;
    }

    public Reminder getOwnedReminderById(Application application, Long reminderId) {
        return application.getReminders().stream()
                .filter(reminder -> reminderId.equals(reminder.getId()))
                .findFirst()
                .orElseThrow(() -> new ReminderNotFoundException(reminderId));
    }

    public List<ReminderResponse> getRemindersForApplication(Long applicationId) {
        Application application = applicationService.getOwnedApplicationById(applicationId);
        return application.getReminders().stream().map(ReminderResponse::from).toList();
    }

    @Transactional
    public ReminderResponse create(Long applicationId, ReminderRequest reminderRequest) {
        Application application = applicationService.getOwnedApplicationById(applicationId);
        Reminder reminder = new Reminder();
        reminder.setReminderDate(reminderRequest.reminderDate());
        reminder.setCommentary(reminderRequest.commentary());
        reminder.setContactType(reminderRequest.contactTypeForReminder());

        application.addReminder(reminder);

        return ReminderResponse.from(reminderRepository.save(reminder));
    }

    @Transactional
    public ReminderResponse update(Long applicationId, Long reminderId, ReminderRequest reminderRequest) {
        Application application = applicationService.getOwnedApplicationById(applicationId);

        Reminder reminder = getOwnedReminderById(application, reminderId);

        reminder.setReminderDate(reminderRequest.reminderDate());
        reminder.setCommentary(reminderRequest.commentary());
        reminder.setContactType(reminderRequest.contactTypeForReminder());

        return ReminderResponse.from(reminder);
    }

    @Transactional
    public void delete(Long applicationId, Long reminderId) {
        Application application = applicationService.getOwnedApplicationById(applicationId);
        Reminder reminder = getOwnedReminderById(application, reminderId);
        application.removeReminder(reminder);
    }

}
