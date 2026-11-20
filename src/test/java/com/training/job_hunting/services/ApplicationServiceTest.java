package com.training.job_hunting.services;

import com.training.job_hunting.auth.CurrentUserProvider;
import com.training.job_hunting.dtos.ApplicationRequest;
import com.training.job_hunting.exceptions.ApplicationNotFoundException;
import com.training.job_hunting.exceptions.InvalidReferenceException;
import com.training.job_hunting.models.Application;
import com.training.job_hunting.models.ApplicationStatus;
import com.training.job_hunting.models.Enterprise;
import com.training.job_hunting.models.User;
import com.training.job_hunting.repositories.ApplicationRepository;
import com.training.job_hunting.repositories.EnterpriseRepository;
import com.training.job_hunting.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private EnterpriseService enterpriseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(currentUserProvider, userRepository, applicationRepository, enterpriseService);
    }

    @Test
    void create_throwsInvalidReference_whenCompanyNotOwned() {
        // Given
        ApplicationRequest applicationRequest = new ApplicationRequest(
                "Test job", "Test job description",
                "www.url.com", 1000L,
                "Nantes", LocalDate.now(), "",
                ApplicationStatus.YET_TO_SEND_RESUME, 42L
        );
        when(enterpriseService.findOwnedEnterprise(42L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> applicationService.create(applicationRequest))
                .isInstanceOf(InvalidReferenceException.class)
                .hasFieldOrPropertyWithValue("field", "enterpriseId");
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void create_setsDefaultStatus_whenStatusMissing() {
        // Given
        ApplicationRequest applicationRequest = new ApplicationRequest(
                "Test job", "Test job description",
                "www.url.com", 1000L,
                "Nantes", LocalDate.now(), "",
                null, 1L
        );
        User currentUser = new User();
        Enterprise enterprise = new Enterprise();
        enterprise.setName("Nantes Digital");
        enterprise.setHeadquarters("Nantes");

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.getReferenceById(1L)).thenReturn(currentUser);
        when(enterpriseService.findOwnedEnterprise(1L)).thenReturn(Optional.of(enterprise));
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        applicationService.create(applicationRequest);

        // Then
        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        Application saved = captor.getValue();
        assertThat(saved.getStatus()).isSameAs(ApplicationStatus.YET_TO_SEND_RESUME);
    }

    @Test
    void update_throwsApplicationNotFound_whenApplicationNotExists_withoutCheckingEnterprise() {
        // Given
        ApplicationRequest applicationRequest = new ApplicationRequest(
                "Job test", "Job description",
                "www.url.com", 1000L,
                "Nantes", LocalDate.now(), "",
                null, 1L);
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(applicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> applicationService.update(1L, applicationRequest))
                .isInstanceOf(ApplicationNotFoundException.class);
        verify(enterpriseService, never()).findOwnedEnterprise(any());
    }
}
