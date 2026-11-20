package com.training.job_hunting.services;

import com.training.job_hunting.auth.CurrentUserProvider;
import com.training.job_hunting.dtos.EnterpriseRequest;
import com.training.job_hunting.dtos.EnterpriseResponse;
import com.training.job_hunting.exceptions.EnterpriseNotFoundException;
import com.training.job_hunting.models.Enterprise;
import com.training.job_hunting.models.User;
import com.training.job_hunting.repositories.EnterpriseRepository;
import com.training.job_hunting.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnterpriseServiceTest {

    @Mock
    private EnterpriseRepository enterpriseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    private EnterpriseService enterpriseService;

    @BeforeEach
    void setUp() {
        enterpriseService = new EnterpriseService(currentUserProvider, userRepository, enterpriseRepository);
    }

    @Test
    void findById_returnsEnterprise_whenOwnedByCurrentUser() {
        // Given
        Enterprise enterprise = new Enterprise();
        enterprise.setName("Nantes Digital");
        enterprise.setHeadquarters("Nantes");
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(enterpriseRepository.findByIdAndUserId(42L, 1L)).thenReturn(Optional.of(enterprise));

        // When
        EnterpriseResponse response = enterpriseService.findById(42L);

        // Then
        assertThat(response.name()).isEqualTo("Nantes Digital");
        assertThat(response.headquarters()).isEqualTo("Nantes");
    }

    @Test
    void findById_throwsNotFound_whenEnterpriseNotOwned() {
        // Given
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(enterpriseRepository.findByIdAndUserId(42L, 1L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> enterpriseService.findById(42L))
                .isInstanceOf(EnterpriseNotFoundException.class);
    }

    @Test
    void create_savesEnterpriseOwnedByCurrentUser() {
        // Given
        User currentUser = new User();
        EnterpriseRequest enterpriseRequest = new EnterpriseRequest("EnterpriseTest", "Nantes");

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepository.getReferenceById(1L)).thenReturn(currentUser);
        when(enterpriseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));


        // When
        enterpriseService.create(enterpriseRequest);

        // Then
        ArgumentCaptor<Enterprise> captor = ArgumentCaptor.forClass(Enterprise.class);
        verify(enterpriseRepository).save(captor.capture());
        Enterprise saved = captor.getValue();
        assertThat(saved.getUser()).isSameAs(currentUser);
        assertThat(saved.getName()).isEqualTo("EnterpriseTest");
        assertThat(saved.getHeadquarters()).isEqualTo("Nantes");
    }


    @Test
    void delete_verifyDeleteCalled_whenOwnedByCurrentUser() {
        // Given
        Enterprise enterprise = new Enterprise();
        enterprise.setName("Nantes Digital");
        enterprise.setHeadquarters("Nantes");
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(enterpriseRepository.findByIdAndUserId(42L, 1L)).thenReturn(Optional.of(enterprise));

        // When
        enterpriseService.delete(42L);

        // Then
        verify(enterpriseRepository).delete(enterprise);
    }

    @Test
    void delete_throwsNotFound_whenEnterpriseNotOwned() {
        // Given
        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(enterpriseRepository.findByIdAndUserId(42L, 1L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> enterpriseService.delete(42L))
                .isInstanceOf(EnterpriseNotFoundException.class);
        verify(enterpriseRepository, never()).delete(any());
    }
}