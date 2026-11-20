package com.training.job_hunting.repositories;

import com.training.job_hunting.models.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByApplicationUserId(Long userId);

    Optional<Reminder> findByIdAndApplicationUserId(Long id, Long userId);
}
