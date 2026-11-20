package com.training.job_hunting.repositories;

import com.training.job_hunting.models.Application;
import com.training.job_hunting.models.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @EntityGraph(attributePaths = {"enterprise", "reminders"})
    Page<Application> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"enterprise", "reminders"})
    Optional<Application> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"enterprise", "reminders"})
    Page<Application> findByUserIdAndStatus(Long userId, ApplicationStatus status, Pageable pageable);
}
