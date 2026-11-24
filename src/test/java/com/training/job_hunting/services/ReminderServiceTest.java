package com.training.job_hunting.services;


import com.training.job_hunting.dtos.ReminderRequest;
import com.training.job_hunting.exceptions.ReminderNotFoundException;
import com.training.job_hunting.models.*;
import com.training.job_hunting.repositories.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private ApplicationService applicationService;

    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        reminderService = new ReminderService(reminderRepository, applicationService);
    }

    @Test
    void create_savesReminder() {
        // Given
        Application application = new Application();

        ReminderRequest reminderRequest = new ReminderRequest(LocalDate.now(),"Pas de commentaires", ContactTypeForReminder.EMAIL);

        when(applicationService.getOwnedApplicationById(1L)).thenReturn(application);
        when(reminderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        reminderService.create(1L, reminderRequest);

        // Then
        ArgumentCaptor<Reminder> reminderArgumentCaptor = ArgumentCaptor.forClass(Reminder.class);
        verify(reminderRepository).save(reminderArgumentCaptor.capture());
        Reminder reminderCapture = reminderArgumentCaptor.getValue();
        assertThat(reminderCapture.getApplication()).isSameAs(application);
        assertThat(application.getReminders()).containsExactly(reminderCapture);
        assertThat(reminderCapture.getContactType()).isEqualTo(ContactTypeForReminder.EMAIL);
    }

    @Test
    void delete_removeRemindersFromApplication() {
        // Given
        Reminder reminder = new Reminder();
        Application application = new Application();
        ReflectionTestUtils.setField(reminder, "id", 1L);
        application.addReminder(reminder);

        when(applicationService.getOwnedApplicationById(1L)).thenReturn(application);

        // When
        reminderService.delete(1L, 1L);

        // Then
        assertThat(application.getReminders()).isEmpty();
        assertThat(reminder.getApplication()).isNull();
    }

    @Test
    void delete_throwsReminderNotFound_whenReminderDoesNotBelongToApplication() {
        // Given
        Reminder ownReminder = new Reminder();
        Application application = new Application();
        ReflectionTestUtils.setField(ownReminder, "id", 2L);
        application.addReminder(ownReminder);

        Reminder otherReminder = new Reminder();
        Application otherApplication = new Application();
        ReflectionTestUtils.setField(otherReminder, "id", 1L);
        otherApplication.addReminder(otherReminder);



        when(applicationService.getOwnedApplicationById(1L)).thenReturn(application);

        // When / Then
        assertThatThrownBy(() -> reminderService.delete(1L, 1L))
                .isInstanceOf(ReminderNotFoundException.class);
        assertThat(application.getReminders()).containsExactly(ownReminder);
        assertThat(otherReminder.getApplication()).isSameAs(otherApplication);
    }
}
