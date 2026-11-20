package com.training.job_hunting.services;


import com.training.job_hunting.auth.CurrentUserProvider;
import com.training.job_hunting.dtos.EnterpriseRequest;
import com.training.job_hunting.dtos.EnterpriseResponse;
import com.training.job_hunting.exceptions.EnterpriseNotFoundException;
import com.training.job_hunting.models.Enterprise;
import com.training.job_hunting.repositories.EnterpriseRepository;
import com.training.job_hunting.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EnterpriseService {

    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(CurrentUserProvider currentUserProvider, UserRepository userRepository, EnterpriseRepository enterpriseRepository) {
        this.currentUserProvider = currentUserProvider;
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    public List<EnterpriseResponse> findAll() {
        return enterpriseRepository.findByUserId(currentUserProvider.getCurrentUserId())
                .stream()
                .map(EnterpriseResponse::from)
                .toList();
    }

    public EnterpriseResponse findById(Long id) {
        return EnterpriseResponse.from(getOwnedEnterprise(id));
    }

    @Transactional
    public EnterpriseResponse create(EnterpriseRequest enterpriseRequest) {
        Enterprise enterprise = new Enterprise();
        enterprise.setName(enterpriseRequest.name());
        enterprise.setHeadquarters(enterpriseRequest.headquarters());
        enterprise.setUser(userRepository.getReferenceById(
                currentUserProvider.getCurrentUserId()));
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        return EnterpriseResponse.from(savedEnterprise);
    }

    @Transactional
    public EnterpriseResponse update(Long id, EnterpriseRequest enterpriseRequest) {
        Enterprise enterprise = getOwnedEnterprise(id);
        enterprise.setName(enterpriseRequest.name());
        enterprise.setHeadquarters(enterpriseRequest.headquarters());

        return EnterpriseResponse.from(enterpriseRepository.save(enterprise));
    }

    @Transactional
    public void  delete(Long id) {
        Enterprise enterprise = getOwnedEnterprise(id);
        enterpriseRepository.delete(enterprise);
    }

    public Optional<Enterprise> findOwnedEnterprise(Long id) {
        return enterpriseRepository.findByIdAndUserId(id, currentUserProvider.getCurrentUserId());
    }

    private Enterprise getOwnedEnterprise(Long id) {
        return findOwnedEnterprise(id)
                .orElseThrow(() -> new EnterpriseNotFoundException(id));
    }
}
