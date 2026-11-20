package com.training.job_hunting.repositories;

import com.training.job_hunting.models.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

    List<Enterprise> findByUserId(Long userId);

    Optional<Enterprise> findByIdAndUserId(Long id, Long userId);
}
