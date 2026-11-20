package com.training.job_hunting.repositories;

import com.training.job_hunting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
