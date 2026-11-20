package com.training.job_hunting.services;

import com.training.job_hunting.auth.CurrentUserProvider;
import com.training.job_hunting.dtos.ApplicationRequest;
import com.training.job_hunting.dtos.ApplicationResponse;
import com.training.job_hunting.exceptions.ApplicationNotFoundException;
import com.training.job_hunting.exceptions.InvalidReferenceException;
import com.training.job_hunting.exceptions.InvalidSortOptionsException;
import com.training.job_hunting.models.Application;
import com.training.job_hunting.models.ApplicationStatus;
import com.training.job_hunting.models.Enterprise;
import com.training.job_hunting.repositories.ApplicationRepository;
import com.training.job_hunting.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final EnterpriseService enterpriseService;

    private static final Set<String> SORTABLE_PROPERTIES = Set.of("jobTitle", "status", "applicationDate", "location");

    public ApplicationService(CurrentUserProvider currentUserProvider, UserRepository userRepository, ApplicationRepository applicationRepository, EnterpriseService enterpriseService) {
        this.currentUserProvider = currentUserProvider;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.enterpriseService = enterpriseService;
    }

    public ApplicationResponse findById(Long id) {
        return ApplicationResponse.from(this.getOwnedApplicationById(id));
    }

    Application getOwnedApplicationById(Long id) {
        return applicationRepository.findByIdAndUserId(id, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ApplicationNotFoundException(id));
    }

    public Page<ApplicationResponse> findByStatus(ApplicationStatus status, Pageable pageable) {
        Long userId = currentUserProvider.getCurrentUserId();
        authorizedSortProperties(pageable);
        Page<Application> applications = (status != null) ?
                applicationRepository.findByUserIdAndStatus(userId, status, pageable)
                : applicationRepository.findByUserId(userId, pageable);
        return applications.map(ApplicationResponse::from);
    }

    private void authorizedSortProperties(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (!SORTABLE_PROPERTIES.contains(order.getProperty())) {
                throw new InvalidSortOptionsException("sort", order.getProperty());
            }
        }
    }

    @Transactional
    public ApplicationResponse create(ApplicationRequest applicationRequest){
        Enterprise enterprise = this.enterpriseService.findOwnedEnterprise(applicationRequest.enterpriseId())
                .orElseThrow(() -> new InvalidReferenceException("enterpriseId", applicationRequest.enterpriseId()));

        Application application = new Application();
        application.setEnterprise(enterprise);
        application.setJobTitle(applicationRequest.jobTitle());
        application.setJobDescription(applicationRequest.jobDescription());
        application.setStatus((applicationRequest.status() != null)
                ? applicationRequest.status()
                : ApplicationStatus.YET_TO_SEND_RESUME);
        application.setOfferLink(applicationRequest.offerLink());
        application.setLocation(applicationRequest.location());
        application.setNotes(applicationRequest.notes());
        application.setMonthlySalary(applicationRequest.monthlySalary());
        application.setApplicationDate(applicationRequest.applicationDate());

        application.setUser(userRepository.getReferenceById(
                currentUserProvider.getCurrentUserId()));
        return ApplicationResponse.from(applicationRepository.save(application));
    }

    @Transactional
    public ApplicationResponse update(Long id, ApplicationRequest applicationRequest) {

        Application application = getOwnedApplicationById(id);

        application.setJobTitle(applicationRequest.jobTitle());
        application.setJobDescription(applicationRequest.jobDescription());
        application.setStatus((applicationRequest.status() != null)
                ? applicationRequest.status()
                : ApplicationStatus.YET_TO_SEND_RESUME);
        application.setOfferLink(applicationRequest.offerLink());
        application.setLocation(applicationRequest.location());
        application.setNotes(applicationRequest.notes());
        application.setMonthlySalary(applicationRequest.monthlySalary());
        application.setApplicationDate(applicationRequest.applicationDate());
        Enterprise enterprise = this.enterpriseService.findOwnedEnterprise(applicationRequest.enterpriseId())
                .orElseThrow(() -> new InvalidReferenceException("enterpriseId", applicationRequest.enterpriseId()));
        application.setEnterprise(enterprise);

        return ApplicationResponse.from(application);
    }

    @Transactional
    public void delete(Long id) {
        Application application = getOwnedApplicationById(id);
        applicationRepository.delete(application);
    }
}
